package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.dashscope.*;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class QwenModelProvider implements ModelProvider {
    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        return QwenChatModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .maxTokens(options.getMaxTokens())
                .temperature(options.getTemperature().floatValue())
                .build();
    }

    @Override
    public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
        return QwenStreamingChatModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .maxTokens(options.getMaxTokens())
                .temperature(options.getTemperature().floatValue())
                .build();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        return QwenEmbeddingModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .build();
    }

    @Override
    public ScoringModel scoringModel(ScoringModelOptions options) {
        return new DisabledScoringModel();
    }

    @Override
    public ImageModel imageModel(ImageModelOptions options) {
        return WanxImageModel.builder()
                .baseUrl(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .modelName(options.getModelName())
                .style(WanxImageStyle.valueOf(options.getStyle()))
                .size(WanxImageSize.valueOf(options.getSize()))
                .build();
    }

    @Override
    public ModerationModel moderationModel(ModerationModelOptions options) {
        return new DisabledModerationModel();
    }

    @Override
    public ModelProviderEnum modelProvider() {
        return ModelProviderEnum.QWEN;
    }

    @Override
    public List<ModelTypeEnum> supportedModelTypes() {
        return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING, ModelTypeEnum.IMAGE);
    }
}
