package ai.llmchat.common.langchain.rag;

import ai.llmchat.common.langchain.enums.ResponseFormatEnum;
import ai.llmchat.common.langchain.model.ModelProviderFactory;
import ai.llmchat.common.langchain.model.options.LanguageModelOptions;
import ai.llmchat.common.langchain.rag.options.PromptOptions;
import ai.llmchat.common.langchain.rag.options.SuggestedOptions;
import ai.llmchat.common.langchain.rag.output.Prompt;
import ai.llmchat.common.langchain.rag.output.Suggestion;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class AIAssistantService {

	private final ChatMemoryStore chatMemoryStore;

	private final ModelProviderFactory modelProviderFactory;

	public AIAssistantService(ChatMemoryStore chatMemoryStore, ModelProviderFactory modelProviderFactory) {
		this.chatMemoryStore = chatMemoryStore;
		this.modelProviderFactory = modelProviderFactory;
	}

	public Prompt optimizePrompt(PromptOptions options) {
		LanguageModelOptions modelOptions = new LanguageModelOptions();
		modelOptions.setTemperature(0.1);
		modelOptions.setNumCtx(8192);
		modelOptions.setMaxTokens(2048);
		modelOptions.setModelProvider(options.getModelProvider());
		modelOptions.setModelName(options.getModelName());
		modelOptions.setModelType(options.getModelType());
		modelOptions.setBaseUrl(options.getBaseUrl());
		modelOptions.setApiKey(options.getApiKey());
		modelOptions.setSecretKey(options.getSecretKey());
		modelOptions.setTimeout(Duration.ofSeconds(30));
		modelOptions.setMaxRetries(3);
		modelOptions.setLogRequests(true);
		modelOptions.setLogResponses(true);

		String prompt = AiServices.builder(AIAssistant.class)
			.chatLanguageModel(modelProviderFactory.chatLanguageModel(modelOptions))
			.build()
			.optimizePrompt(options.getInstruction());

		modelOptions.setFormat(ResponseFormatEnum.JSON_OBJECT);
		return AiServices.builder(AIAssistant.class)
			.chatLanguageModel(modelProviderFactory.chatLanguageModel(modelOptions))
			.build()
			.extractPrompt(options.getInstruction(), prompt);
	}

	public List<String> suggestedQuestions(SuggestedOptions options) {
		LanguageModelOptions modelOptions = new LanguageModelOptions();
		modelOptions.setTemperature(0.1);
		modelOptions.setNumCtx(8192);
		modelOptions.setMaxTokens(2048);
		modelOptions.setModelProvider(options.getModelProvider());
		modelOptions.setModelName(options.getModelName());
		modelOptions.setModelType(options.getModelType());
		modelOptions.setBaseUrl(options.getBaseUrl());
		modelOptions.setApiKey(options.getApiKey());
		modelOptions.setSecretKey(options.getSecretKey());
		modelOptions.setTimeout(Duration.ofSeconds(30));
		modelOptions.setMaxRetries(3);
		modelOptions.setLogRequests(true);
		modelOptions.setLogResponses(true);
		modelOptions.setFormat(ResponseFormatEnum.JSON_OBJECT);
		List<ChatMessage> messages = chatMemoryStore.getMessages(options.getChatId());
		if (CollectionUtils.isEmpty(messages)) {
			return List.of();
		}
		String chatHistories = messages.stream().map(item -> {
			if (item.type() == ChatMessageType.USER) {
				return "Human:" + item.text();
			}
			else if (item.type() == ChatMessageType.AI) {
				return "Assistant:" + item.text();
			}
			return StringUtils.EMPTY;
		}).collect(Collectors.joining(System.lineSeparator()));
		if (StringUtils.isBlank(chatHistories)) {
			return List.of();
		}
		Suggestion suggestion = AiServices.builder(AIAssistant.class)
			.chatLanguageModel(modelProviderFactory.chatLanguageModel(modelOptions))
			.build()
			.suggestedQuestions(chatHistories);
		return suggestion.getQuestions();
	}

}
