package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.enums.ResponseFormatEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.openai.*;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class OpenAiModelProvider implements ModelProvider {

	@Override
	public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
		String responseFormat = null;
		if (options.getFormat() == ResponseFormatEnum.JSON_OBJECT) {
			responseFormat = "json_object";
		}
		return new OpenAiChatModel.OpenAiChatModelBuilder().baseUrl(options.getBaseUrl())
			.apiKey(options.getApiKey())
			.modelName(options.getModelName())
			.maxTokens(options.getMaxTokens())
			.temperature(options.getTemperature())
			.responseFormat(responseFormat)
			.timeout(options.getTimeout())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
		String responseFormat = null;
		if (options.getFormat() == ResponseFormatEnum.JSON_OBJECT) {
			responseFormat = "json_object";
		}
		return new OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder().baseUrl(options.getBaseUrl())
			.apiKey(options.getApiKey())
			.modelName(options.getModelName())
			.maxTokens(options.getMaxTokens())
			.temperature(options.getTemperature())
			.responseFormat(responseFormat)
			.timeout(options.getTimeout())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
		return new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder().baseUrl(options.getBaseUrl())
			.apiKey(options.getApiKey())
			.modelName(options.getModelName())
			.dimensions(options.getDimensions())
			.timeout(options.getTimeout())
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
		return new OpenAiImageModel.OpenAiImageModelBuilder().baseUrl(options.getBaseUrl())
			.apiKey(options.getApiKey())
			.modelName(options.getModelName())
			.style(options.getStyle())
			.quality(options.getQuality())
			.size(options.getSize())
			.timeout(options.getTimeout())
			.maxRetries(options.getMaxRetries())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public ModerationModel moderationModel(ModerationModelOptions options) {
		return new OpenAiModerationModel.OpenAiModerationModelBuilder().baseUrl(options.getBaseUrl())
			.apiKey(options.getApiKey())
			.modelName(options.getModelName())
			.timeout(options.getTimeout())
			.maxRetries(options.getMaxRetries())
			.logRequests(options.getLogRequests())
			.logResponses(options.getLogResponses())
			.build();
	}

	@Override
	public ModelProviderEnum modelProvider() {
		return ModelProviderEnum.OPEN_AI;
	}

	@Override
	public List<ModelTypeEnum> supportedModelTypes() {
		return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING, ModelTypeEnum.IMAGE, ModelTypeEnum.MODERATION);
	}

}
