package ai.llmchat.common.langchain.config;

import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.ModelProviderFactory;
import ai.llmchat.common.langchain.model.impl.*;
import ai.llmchat.common.langchain.rag.AIAssistantService;
import ai.llmchat.common.langchain.rag.StreamingChatService;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelProviderConfiguration {
    @Bean
    public OllamaModelProvider ollamaModelProvider() {
        return new OllamaModelProvider();
    }

    @Bean
    public AzureOpenAiModelProvider azureOpenAiModelProvider() {
        return new AzureOpenAiModelProvider();
    }

    @Bean
    public HuggingFaceModelProvider huggingFaceModelProvider() {
        return new HuggingFaceModelProvider();
    }

    @Bean
    public LocalAiModelProvider localAiModelProvider() {
        return new LocalAiModelProvider();
    }

    @Bean
    public OpenAiModelProvider openAiModelProvider() {
        return new OpenAiModelProvider();
    }

    @Bean
    public QianfanModelProvider qianfanModelProvider() {
        return new QianfanModelProvider();
    }

    @Bean
    public ZhipuAiModelProvider zhipuAiModelProvider() {
        return new ZhipuAiModelProvider();
    }

    @Bean
    public ChatGlmModelProvider chatGlmModelProvider() {
        return new ChatGlmModelProvider();
    }

    @Bean
    public ModelProviderFactory modelProviderFactory(List<ModelProvider> modelProviders) {
        return new DefaultModelProviderFactory(modelProviders);
    }

    @Bean
    public StreamingChatService chatAssistantService(ContentStore contentStore,
                                                     ChatMemoryStore chatMemoryStore,
                                                     ModelProviderFactory modelProviderFactory) {
        return new StreamingChatService(contentStore, chatMemoryStore, modelProviderFactory);
    }

    @Bean
    public AIAssistantService promptAssistantService(ChatMemoryStore chatMemoryStore, ModelProviderFactory modelProviderFactory) {
        return new AIAssistantService(chatMemoryStore, modelProviderFactory);
    }
}
