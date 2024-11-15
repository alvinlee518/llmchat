package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.model.localai.LocalAiEmbeddingModel;
import dev.langchain4j.model.localai.LocalAiStreamingChatModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class LocalAiModelProvider implements ModelProvider {
    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        return LocalAiChatModel.builder()
                .baseUrl(options.getBaseUrl())
                .modelName(options.getModelName())
                .maxTokens(options.getMaxTokens())
                .temperature(options.getTemperature())
                .timeout(options.getTimeout())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
        return LocalAiStreamingChatModel.builder()
                .baseUrl(options.getBaseUrl())
                .modelName(options.getModelName())
                .maxTokens(options.getMaxTokens())
                .temperature(options.getTemperature())
                .timeout(options.getTimeout())
                .logRequests(options.getLogRequests())
                .logResponses(options.getLogResponses())
                .build();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        return LocalAiEmbeddingModel.builder()
                .baseUrl(options.getBaseUrl())
                .modelName(options.getModelName())
                .timeout(options.getTimeout())
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
        return ModelProviderEnum.LOCAL_AI;
    }

    @Override
    public List<ModelTypeEnum> supportedModelTypes() {
        return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING);
    }
}
