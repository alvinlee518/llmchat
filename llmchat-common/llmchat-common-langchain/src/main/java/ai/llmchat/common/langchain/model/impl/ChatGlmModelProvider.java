package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.community.model.chatglm.ChatGlmChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.DisabledStreamingChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.DisabledEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class ChatGlmModelProvider implements ModelProvider {

	@Override
	public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
		return ChatGlmChatModel.builder()
			.baseUrl(options.getBaseUrl())
			.temperature(options.getTemperature())
			.timeout(options.getTimeout())
			.maxRetries(options.getMaxRetries())
			.maxLength(options.getMaxTokens())
			.build();
	}

	@Override
	public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
		return new DisabledStreamingChatLanguageModel();
	}

	@Override
	public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
		return new DisabledEmbeddingModel();
	}

	@Override
	public ScoringModel scoringModel(ScoringModelOptions options) {
		return new DisabledScoringModel();
	}

	@Override
	public ImageModel imageModel(ImageModelOptions options) {
		return new DisabledImageModel();
	}

	@Override
	public ModerationModel moderationModel(ModerationModelOptions options) {
		return new DisabledModerationModel();
	}

	@Override
	public ModelProviderEnum modelProvider() {
		return ModelProviderEnum.CHAT_GLM;
	}

	@Override
	public List<ModelTypeEnum> supportedModelTypes() {
		return List.of(ModelTypeEnum.LLM);
	}

}
