package ai.llmchat.common.langchain.rag;

import ai.llmchat.common.langchain.model.ModelProviderFactory;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import ai.llmchat.common.langchain.rag.content.retriever.ContentStoreRetriever;
import ai.llmchat.common.langchain.rag.options.ChatOptions;
import ai.llmchat.common.langchain.rag.options.ContentOptions;
import ai.llmchat.common.langchain.rag.options.RetrievalOptions;
import ai.llmchat.common.langchain.rag.output.Citation;
import ai.llmchat.common.langchain.rag.output.Message;
import ai.llmchat.common.langchain.rag.output.Usage;
import ai.llmchat.common.langchain.rag.transformer.RewriteQueryTransformer;
import ai.llmchat.common.langchain.util.LangchainConstants;
import cn.hutool.core.thread.NamedThreadFactory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.DefaultQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class StreamingChatService {
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2 + 10,
            1,
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(256),
            new NamedThreadFactory("Langchain-", false),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private final ContentStore contentStore;
    private final ChatMemoryStore chatMemoryStore;
    private final ModelProviderFactory modelProviderFactory;

    public StreamingChatService(ContentStore contentStore, ChatMemoryStore chatMemoryStore, ModelProviderFactory modelProviderFactory) {
        this.contentStore = contentStore;
        this.chatMemoryStore = chatMemoryStore;
        this.modelProviderFactory = modelProviderFactory;
    }

    public Flux<Message> streamingChat(ChatOptions options) {
        long start = System.currentTimeMillis();
        ChatLanguageModel languageModel = modelProviderFactory.chatLanguageModel(options.getModelOptions());
        StreamingChatLanguageModel streamingLanguageModel = modelProviderFactory.streamingChatLanguageModel(options.getModelOptions());
        AiServices<AIAssistant> aiServices = AiServices.builder(AIAssistant.class)
                .chatLanguageModel(languageModel)
                .streamingChatLanguageModel(streamingLanguageModel)
                .systemMessageProvider(memoryId -> options.getSystemPrompt())
                .toolProvider(options.getToolProvider())
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .id(memoryId)
                        .maxMessages(options.getMaxMemory())
                        .build());
        if (Objects.nonNull(options.getRetrieval())) {
            RetrievalAugmentor retrievalAugmentor = getRetrievalAugmentor(languageModel, options.getRetrieval());
            aiServices.retrievalAugmentor(retrievalAugmentor);
        }

        AIAssistant aiAssistant = aiServices.build();

        Sinks.Many<Message> sink = Sinks.many().unicast().onBackpressureBuffer();
        try {
            TokenStream tokenStream = aiAssistant.streamingChat(options.getChatId(), options.getInstruction());
            AtomicReference<List<Citation>> citations = new AtomicReference<>(new ArrayList<>());
            tokenStream.onRetrieved(contents -> {
                List<Long> segmentIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(contents)) {
                    segmentIds = contents.stream().filter(item -> Objects.nonNull(item)
                                    && Objects.nonNull(item.textSegment())
                                    && Objects.nonNull(item.textSegment().metadata())
                            )
                            .map(item -> item.textSegment()
                                    .metadata()
                                    .getLong(LangchainConstants.METADATA_FIELD_PARAGRAPH)
                            )
                            .toList();
                }
                Function<List<Long>, List<Citation>> citationProvider = options.getCitationProvider();
                if (Objects.nonNull(citationProvider)) {
                    List<Citation> apply = citationProvider.apply(segmentIds);
                    citations.set(apply);
                }
            });
            tokenStream.onNext(text -> {
                if (StringUtils.isNotBlank(text)) {
                    sink.tryEmitNext(Message.next(options.getId(), options.getChatId(), text));
                }
            });
            tokenStream.onComplete(response -> {
                TokenUsage tokenUsage = response.tokenUsage();
                Usage usage = null;
                if (Objects.nonNull(tokenUsage)) {
                    usage = Usage.builder()
                            .duration((System.currentTimeMillis() - start))
                            .promptTokens(tokenUsage.inputTokenCount())
                            .completionTokens(tokenUsage.outputTokenCount())
                            .totalTokens(tokenUsage.totalTokenCount())
                            .build();
                }
                List<Citation> list = citations.get();
                Message message = Message.complete(options.getId(), options.getChatId(), response.content().text(), usage, list);
                tryEmitComplete(sink, message, options.getDoOnComplete());
            });
            tokenStream.onError(throwable -> {
                log.error(throwable.getMessage(), throwable);
                Message message = Message.error(options.getId(), options.getChatId(), throwable.getMessage(), (System.currentTimeMillis() - start));
                tryEmitComplete(sink, message, options.getDoOnComplete());
            });
            tokenStream.start();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            Message message = Message.error(options.getId(), options.getChatId(), ex.getMessage(), (System.currentTimeMillis() - start));
            tryEmitComplete(sink, message, options.getDoOnComplete());
        }
        return sink.asFlux();
    }

    private void tryEmitComplete(Sinks.Many<Message> sink, Message complete, Consumer<Message> consumer) {
        sink.tryEmitNext(complete);
        sink.tryEmitComplete();
        if (Objects.nonNull(consumer)) {
            POOL_EXECUTOR.submit(() -> {
                try {
                    consumer.accept(complete);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    private RetrievalAugmentor getRetrievalAugmentor(ChatLanguageModel chatModel, RetrievalOptions options) {
        QueryTransformer queryTransformer = getQueryTransformer(chatModel, options);
        QueryRouter queryRouter = getQueryRouter(options);
        ContentAggregator contentAggregator = getContentAggregator(options);
        ContentInjector contentInjector = DefaultContentInjector.builder()
                .promptTemplate(PromptTemplate.from(PromptConstants.QA_CONTEXT_PROMPT))
                .build();
        return DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .queryRouter(queryRouter)
                .contentAggregator(contentAggregator)
                .contentInjector(contentInjector)
                .executor(POOL_EXECUTOR)
                .build();
    }

    private QueryTransformer getQueryTransformer(ChatLanguageModel chatModel, RetrievalOptions options) {
        QueryTransformer queryTransformer = new DefaultQueryTransformer();
        if (Objects.nonNull(chatModel)) {
            if (Optional.ofNullable(options.getRewriteEnabled()).orElse(false)) {
                queryTransformer = new RewriteQueryTransformer(chatModel);
            }
        }
        return queryTransformer;
    }

    private QueryRouter getQueryRouter(RetrievalOptions options) {
        List<ContentRetriever> contentRetrieverList = new ArrayList<>();
        for (ContentOptions content : options.getContents()) {
            ContentStoreRetriever contentRetriever = ContentStoreRetriever.builder()
                    .contentStore(contentStore)
                    .embeddingModel(modelProviderFactory.embeddingModel(content.getModelOptions()))
                    .filterProvider(query -> MetadataFilterBuilder.metadataKey(LangchainConstants.METADATA_FIELD_DATASET).isEqualTo(content.getId()))
                    .minScoreProvider(query -> content.getScore())
                    .maxResultsProvider(query -> content.getTopK())
                    .searchModeProvider(query -> content.getSearchMode())
                    .build();
            contentRetrieverList.add(contentRetriever);
        }
        return new DefaultQueryRouter(contentRetrieverList);
    }

    private ContentAggregator getContentAggregator(RetrievalOptions options) {
        if (Objects.nonNull(options.getModelOptions())) {
            ScoringModel scoringModel = modelProviderFactory.scoringModel(options.getModelOptions());
            return ReRankingContentAggregator.builder()
                    .scoringModel(scoringModel)
                    .minScore(options.getScore())
                    .maxResults(options.getTopK())
                    .build();
        }
        return new DefaultContentAggregator();
    }
}
