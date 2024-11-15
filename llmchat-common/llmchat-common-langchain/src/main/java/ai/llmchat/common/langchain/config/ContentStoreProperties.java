package ai.llmchat.common.langchain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(ContentStoreProperties.PREFIX)
public class ContentStoreProperties {
    static final String PREFIX = "langchain4j.embedding";
    private String apiKey;
    private String scheme;
    private String host;
    private String objectClass;
}
