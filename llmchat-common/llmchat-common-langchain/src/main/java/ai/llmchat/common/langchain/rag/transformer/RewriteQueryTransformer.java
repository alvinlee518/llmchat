package ai.llmchat.common.langchain.rag.transformer;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;

public class RewriteQueryTransformer implements QueryTransformer {
    public static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = PromptTemplate.from("""
            Given the following conversation and a follow up question, rephrase the follow up \\
            question to be a standalone question.
            \n
            Chat History:
            {chat_history}
            Follow Up Input: {question}
            Standalone Question:""");

    protected final PromptTemplate promptTemplate;
    protected final ChatLanguageModel chatLanguageModel;

    public RewriteQueryTransformer(ChatLanguageModel chatLanguageModel) {
        this(chatLanguageModel, DEFAULT_PROMPT_TEMPLATE);
    }

    public RewriteQueryTransformer(ChatLanguageModel chatLanguageModel, PromptTemplate promptTemplate) {
        this.chatLanguageModel = ValidationUtils.ensureNotNull(chatLanguageModel, "chatLanguageModel");
        this.promptTemplate = promptTemplate;
    }

    @Override
    public Collection<Query> transform(Query query) {
        List<ChatMessage> chatMemory = query.metadata().chatMemory();
        if (chatMemory.isEmpty()) {
            // no need to compress if there are no previous messages
            return singletonList(query);
        }

        Prompt prompt = createPrompt(query, format(chatMemory));
        String compressedQueryText = chatLanguageModel.generate(prompt.text());
        Query compressedQuery = query.metadata() == null
                ? Query.from(compressedQueryText)
                : Query.from(compressedQueryText, query.metadata());
        return singletonList(compressedQuery);
    }

    protected String format(List<ChatMessage> chatMemory) {
        return chatMemory.stream()
                .map(this::format)
                .filter(Objects::nonNull)
                .collect(joining("\n"));
    }

    protected String format(ChatMessage message) {
        if (message instanceof UserMessage msg) {
            return "User: " + msg.singleText();
        } else if (message instanceof AiMessage aiMessage) {
            if (aiMessage.hasToolExecutionRequests()) {
                return null;
            }
            return "AI: " + aiMessage.text();
        } else {
            return null;
        }
    }

    protected Prompt createPrompt(Query query, String chatMemory) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("query", query.text());
        variables.put("chatMemory", chatMemory);
        return promptTemplate.apply(variables);
    }
}
