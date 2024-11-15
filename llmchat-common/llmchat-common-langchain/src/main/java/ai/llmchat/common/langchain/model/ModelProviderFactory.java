package ai.llmchat.common.langchain.model;

import ai.llmchat.common.langchain.model.options.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.List;

public interface ModelProviderFactory {
    void credentialsValidate(ModelOptions options);

    ChatLanguageModel chatLanguageModel(LanguageModelOptions options);

    StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options);

    EmbeddingModel embeddingModel(EmbeddingModelOptions options);

    ScoringModel scoringModel(ScoringModelOptions options);

    ImageModel imageModel(ImageModelOptions options);

    ModerationModel moderationModel(ModerationModelOptions options);

    List<ModelProvider> getModelProviders();
}
