package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.community.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiStreamingChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class ZhipuAiModelProvider implements ModelProvider {

	@Override
	public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
		return new ZhipuAiChatModel.ZhipuAiChatModelBuilder().baseUrl(options.getBaseUrl())
			.model(options.getModelName())
			.apiKey(options.getApiKey())
			.maxToken(options.getMaxTokens())
			.callTimeout(options.getTimeout())
			.maxRetries(options.getMaxRetries())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
		return new ZhipuAiStreamingChatModel.ZhipuAiStreamingChatModelBuilder().baseUrl(options.getBaseUrl())
			.model(options.getModelName())
			.apiKey(options.getApiKey())
			.maxToken(options.getMaxTokens())
			.callTimeout(options.getTimeout())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
		return new ZhipuAiEmbeddingModel.ZhipuAiEmbeddingModelBuilder().baseUrl(options.getBaseUrl())
			.model(options.getModelName())
			.apiKey(options.getApiKey())
			.callTimeout(options.getTimeout())
			.maxRetries(options.getMaxRetries())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public ScoringModel scoringModel(ScoringModelOptions options) {
		return new DisabledScoringModel();
	}

	@Override
	public ImageModel imageModel(ImageModelOptions options) {
		return ZhipuAiImageModel.builder()
			.baseUrl(options.getBaseUrl())
			.model(options.getModelName())
			.apiKey(options.getApiKey())
			.callTimeout(options.getTimeout())
			.maxRetries(options.getMaxRetries())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public ModerationModel moderationModel(ModerationModelOptions options) {
		return new DisabledModerationModel();
	}

	@Override
	public ModelProviderEnum modelProvider() {
		return ModelProviderEnum.ZHIPU_AI;
	}

	@Override
	public List<ModelTypeEnum> supportedModelTypes() {
		return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING, ModelTypeEnum.IMAGE);
	}

}
