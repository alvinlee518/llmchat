package ai.llmchat.common.security.config;

import ai.llmchat.common.security.endpoint.SecurityEndpoint;
import ai.llmchat.common.security.handler.TokenSecurityInterceptor;
import ai.llmchat.common.security.service.SecurityClaimsService;
import ai.llmchat.common.security.service.TokenStoreService;
import ai.llmchat.common.security.web.RequestLoggingFilter;
import ai.llmchat.common.security.web.RestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurityConfiguration implements WebMvcConfigurer {
    @Autowired
    private TokenStoreService tokenStoreService;
    @Autowired
    private SecurityClaimsService securityClaimsService;
    @Autowired
    private SecurityConfigurationProperties properties;

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RestExceptionHandler globalExceptionHandler() {
        return new RestExceptionHandler();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenSecurityInterceptor(tokenStoreService, securityClaimsService))
                .addPathPatterns(properties.getIncludePatterns())
                .excludePathPatterns(properties.getExcludePatterns());
    }
}
