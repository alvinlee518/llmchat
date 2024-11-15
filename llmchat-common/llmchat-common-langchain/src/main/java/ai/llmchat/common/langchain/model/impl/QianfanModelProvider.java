package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.enums.ResponseFormatEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.qianfan.QianfanChatModel;
import dev.langchain4j.model.qianfan.QianfanEmbeddingModel;
import dev.langchain4j.model.qianfan.QianfanStreamingChatModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class QianfanModelProvider implements ModelProvider {
    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        String responseFormat = null;
        if (options.getFormat() == ResponseFormatEnum.JSON_OBJECT) {
            responseFormat = "json_object";
        }
        return new QianfanChatModel.QianfanChatModelBuilder()
                .endpoint(options.getBaseUrl())
                .modelName(options.getModelName())
                .responseFormat(responseFormat)
                .apiKey(options.getApiKey())
                .secretKey(options.getSecretKey())
                .temperature(options.getTemperature())
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
        return new QianfanStreamingChatModel.QianfanStreamingChatModelBuilder()
                .endpoint(options.getBaseUrl())
                .modelName(options.getModelName())
                .responseFormat(responseFormat)
                .apiKey(options.getApiKey())
                .secretKey(options.getSecretKey())
                .temperature(options.getTemperature())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        return new QianfanEmbeddingModel.QianfanEmbeddingModelBuilder()
                .endpoint(options.getBaseUrl())
                .modelName(options.getModelName())
                .apiKey(options.getApiKey())
                .secretKey(options.getSecretKey())
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
        return new DisabledImageModel();
    }

    @Override
    public ModerationModel moderationModel(ModerationModelOptions options) {
        return new DisabledModerationModel();
    }

    @Override
    public ModelProviderEnum modelProvider() {
        return ModelProviderEnum.QIAN_FAN;
    }

    @Override
    public List<ModelTypeEnum> supportedModelTypes() {
        return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING);
    }
}
