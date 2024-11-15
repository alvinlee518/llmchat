package ai.llmchat.common.core;

import ai.llmchat.common.core.config.DateFormatConfiguration;
import ai.llmchat.common.core.config.JacksonFormatConfiguration;
import ai.llmchat.common.core.config.RedisTemplateConfiguration;
import ai.llmchat.common.core.config.TaskPoolConfiguration;
import ai.llmchat.common.core.util.I18nUtils;
import ai.llmchat.common.core.util.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DateFormatConfiguration.class,
        JacksonFormatConfiguration.class,
        TaskPoolConfiguration.class,
        RedisTemplateConfiguration.class
})
public class CommonCoreAutoConfiguration {
    @Bean
    public I18nUtils i18nUtils() {
        return new I18nUtils();
    }

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
}
