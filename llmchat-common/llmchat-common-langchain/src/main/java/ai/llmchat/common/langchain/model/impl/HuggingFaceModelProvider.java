package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.DisabledStreamingChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public class HuggingFaceModelProvider implements ModelProvider {
    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        return HuggingFaceChatModel.builder()
                .modelId(options.getModelName())
                .accessToken(options.getApiKey())
                .temperature(options.getTemperature())
                .timeout(options.getTimeout())
                .maxNewTokens(options.getMaxTokens())
                .waitForModel(true)
                .build();
    }

    @Override
    public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
        return new DisabledStreamingChatLanguageModel();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        return HuggingFaceEmbeddingModel.builder().modelId(options.getModelName())
                .accessToken(options.getApiKey())
                .timeout(options.getTimeout())
                .waitForModel(true)
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
        return ModelProviderEnum.HUGGING_FACE;
    }

    @Override
    public List<ModelTypeEnum> supportedModelTypes() {
        return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING);
    }
}
