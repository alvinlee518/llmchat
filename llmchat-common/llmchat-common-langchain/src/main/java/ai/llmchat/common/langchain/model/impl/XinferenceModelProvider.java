package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.community.model.xinference.*;
import dev.langchain4j.community.model.xinference.client.image.ResponseFormat;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class XinferenceModelProvider implements ModelProvider {
    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        return XinferenceChatModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .temperature(options.getTemperature())
                .maxTokens(options.getMaxTokens())
                .timeout(options.getTimeout())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
        return XinferenceStreamingChatModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .temperature(options.getTemperature())
                .maxTokens(options.getMaxTokens())
                .timeout(options.getTimeout())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        return XinferenceEmbeddingModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .maxRetries(options.getMaxRetries())
                .timeout(options.getTimeout())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public ScoringModel scoringModel(ScoringModelOptions options) {
        return XinferenceScoringModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .returnDocuments(false)
                .returnLen(true)
                .maxRetries(options.getMaxRetries())
                .timeout(options.getTimeout())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public ImageModel imageModel(ImageModelOptions options) {
        return XinferenceImageModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .negativePrompt(options.getNegativePrompt())
                .responseFormat(ResponseFormat.B64_JSON)
                .size(options.getSize())
                .maxRetries(options.getMaxRetries())
                .timeout(options.getTimeout())
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
        return ModelProviderEnum.XINFERENCE;
    }

    @Override
    public List<ModelTypeEnum> supportedModelTypes() {
        return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING, ModelTypeEnum.RERANK, ModelTypeEnum.IMAGE);
    }
}
