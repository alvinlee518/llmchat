package ai.llmchat.common.langchain;


import ai.llmchat.common.langchain.config.ContentStoreConfiguration;
import ai.llmchat.common.langchain.config.ModelProviderConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({
        ModelProviderConfiguration.class,
        ContentStoreConfiguration.class
})
public class CommonLangchainConfiguration {

}
