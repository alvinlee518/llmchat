package ai.llmchat.common.langchain.model.impl;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.common.langchain.enums.ResponseFormatEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.options.*;
import com.azure.ai.openai.models.ChatCompletionsJsonResponseFormat;
import com.azure.ai.openai.models.ChatCompletionsResponseFormat;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import dev.langchain4j.model.azure.AzureOpenAiImageModel;
import dev.langchain4j.model.azure.AzureOpenAiStreamingChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.moderation.DisabledModerationModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.scoring.ScoringModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AzureOpenAiModelProvider implements ModelProvider {
    @Override
    public ChatLanguageModel chatLanguageModel(LanguageModelOptions options) {
        ChatCompletionsResponseFormat responseFormat = null;
        if (options.getFormat() == ResponseFormatEnum.JSON_OBJECT) {
            responseFormat = new ChatCompletionsJsonResponseFormat();
        }
        return AzureOpenAiChatModel.builder()
                .endpoint(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .deploymentName(options.getModelName())
                .maxTokens(options.getMaxTokens())
                .temperature(options.getTemperature())
                .timeout(options.getTimeout())
                .logRequestsAndResponses(options.getLogRequests())
                .responseFormat(responseFormat)
                .build();
    }

    @Override
    public StreamingChatLanguageModel streamingChatLanguageModel(LanguageModelOptions options) {
        ChatCompletionsResponseFormat responseFormat = null;
        if (options.getFormat() == ResponseFormatEnum.JSON_OBJECT) {
            responseFormat = new ChatCompletionsJsonResponseFormat();
        }
        return AzureOpenAiStreamingChatModel.builder()
                .endpoint(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .deploymentName(options.getModelName())
                .maxTokens(options.getMaxTokens())
                .temperature(options.getTemperature())
                .timeout(options.getTimeout())
                .logRequestsAndResponses(options.getLogRequests())
                .responseFormat(responseFormat)
                .build();
    }

    @Override
    public EmbeddingModel embeddingModel(EmbeddingModelOptions options) {
        return AzureOpenAiEmbeddingModel.builder()
                .endpoint(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .deploymentName(options.getModelName())
                .timeout(options.getTimeout())
                .logRequestsAndResponses(options.getLogRequests())
                .build();
    }

    @Override
    public ScoringModel scoringModel(ScoringModelOptions options) {
        return new DisabledScoringModel();
    }

    @Override
    public ImageModel imageModel(ImageModelOptions options) {
        return AzureOpenAiImageModel.builder()
                .endpoint(options.getBaseUrl())
                .apiKey(options.getApiKey())
                .deploymentName(options.getModelName())
                .style(options.getStyle())
                .quality(options.getQuality())
                .size(options.getSize())
                .timeout(options.getTimeout())
                .maxRetries(options.getMaxRetries())
                .logRequestsAndResponses(options.getLogRequests())
                .build();
    }

    @Override
    public ModerationModel moderationModel(ModerationModelOptions options) {
        return new DisabledModerationModel();
    }

    @Override
    public ModelProviderEnum modelProvider() {
        return ModelProviderEnum.AZURE_OPEN_AI;
    }

    @Override
    public List<ModelTypeEnum> supportedModelTypes() {
        return List.of(ModelTypeEnum.LLM, ModelTypeEnum.EMBEDDING, ModelTypeEnum.IMAGE);
    }
}
