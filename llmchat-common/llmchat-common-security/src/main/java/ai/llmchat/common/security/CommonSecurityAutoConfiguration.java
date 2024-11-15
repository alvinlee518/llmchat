package ai.llmchat.common.security;

import ai.llmchat.common.security.config.SecurityConfiguration;
import ai.llmchat.common.security.config.WebSecurityConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({
        SecurityConfiguration.class,
        WebSecurityConfiguration.class
})
public class CommonSecurityAutoConfiguration {

}
