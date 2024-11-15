package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.ModelProviderFactory;
import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.DisabledChatLanguageModel;
import dev.langchain4j.model.chat.DisabledStreamingChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.DisabledEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

public class DefaultModelProviderFactory implements ModelProviderFactory {
    private final List<ModelProvider> modelProviders;

    public DefaultModelProviderFactory(List<ModelProvider> modelProviders) {
        this.modelProviders = modelProviders;
    }

    @Override
    public void credentialsValidate(ModelOptions options) {
        ModelTypeEnum modelType = ModelTypeEnum.valueOf(options.getModelType());
        switch (modelType) {
            case LLM -> {
                LanguageModelOptions modelOptions = new LanguageModelOptions();
                modelOptions.setMaxTokens(5);
                BeanUtils.copyProperties(options, modelOptions);
                chatLanguageModel(modelOptions).generate(UserMessage.from("ping"));
            }
            case EMBEDDING -> {
                EmbeddingModelOptions modelOptions = new EmbeddingModelOptions();
                BeanUtils.copyProperties(options, modelOptions);
                embeddingModel(modelOptions).embed(TextSegment.from("ping"));
            }
            case RERANK -> {
                ScoringModelOptions modelOptions = new ScoringModelOptions();
                BeanUtils.copyProperties(options, modelOptions);
                scoringModel(modelOptions).score("ping~pong", "ping");
            }
            case IMAGE -> {
                ImageModelOptions modelOptions = new ImageModelOptions();
                BeanUtils.copyProperties(options, modelOptions);
                imageModel(modelOptions).generate("ping");
            }
            default -> {
                throw new UnsupportedOperationException("model type is not supported: " + options.getModelType());
            }
        }
    }

    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        for (ModelProvider provider : modelProviders) {
            if (Objects.equals(options.getModelProvider(), provider.modelProvider().getCode())) {
                return provider.chatLanguageModel(options);
            }
        }
        return new DisabledChatLanguageModel();
    }

    @Override
    public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
        for (ModelProvider provider : modelProviders) {
            if (Objects.equals(options.getModelProvider(), provider.modelProvider().getCode())) {
                return provider.streamingChatLanguageModel(options);
            }
        }
        return new DisabledStreamingChatLanguageModel();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        for (ModelProvider provider : modelProviders) {
            if (Objects.equals(options.getModelProvider(), provider.modelProvider().getCode())) {
                return provider.embeddingModel(options);
            }
        }
        return new DisabledEmbeddingModel();
    }

    @Override
    public ScoringModel scoringModel(ScoringModelOptions options) {
        for (ModelProvider provider : modelProviders) {
            if (Objects.equals(options.getModelProvider(), provider.modelProvider().getCode())) {
                return provider.scoringModel(options);
            }
        }
        return new DisabledScoringModel();
    }

    @Override
    public ImageModel imageModel(ImageModelOptions options) {
        for (ModelProvider provider : modelProviders) {
            if (Objects.equals(options.getModelProvider(), provider.modelProvider().getCode())) {
                return provider.imageModel(options);
            }
        }
        return new DisabledImageModel();
    }

    @Override
    public ModerationModel moderationModel(ModerationModelOptions options) {
        for (ModelProvider provider : modelProviders) {
            if (Objects.equals(options.getModelProvider(), provider.modelProvider().getCode())) {
                return provider.moderationModel(options);
            }
        }
        return new DisabledModerationModel();
    }

    @Override
    public List<ModelProvider> getModelProviders() {
        return modelProviders;
    }
}
