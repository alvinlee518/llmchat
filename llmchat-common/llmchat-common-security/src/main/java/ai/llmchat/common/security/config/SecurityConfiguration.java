package ai.llmchat.common.security.config;

import ai.llmchat.common.security.SecurityClaims;
import ai.llmchat.common.security.endpoint.SecurityEndpoint;
import ai.llmchat.common.security.service.PasswordEncryptService;
import ai.llmchat.common.security.service.SecurityClaimsService;
import ai.llmchat.common.security.service.TokenStoreService;
import ai.llmchat.common.security.service.impl.PasswordEncryptServiceImpl;
import ai.llmchat.common.security.service.impl.TokenStoreServiceImpl;
import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(SecurityConfigurationProperties.class)
public class SecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, SecurityClaims> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SecurityClaims> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用StringRedisSerializer 序列化和反序列化redis的key值
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 使用 fastjson 序列化和反序列化redis的value值
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStoreService tokenStoreService(RedisTemplate<String, SecurityClaims> redisTemplate) {
        return new TokenStoreServiceImpl(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityEndpoint securityEndpoint(TokenStoreService tokenStoreService, SecurityClaimsService securityClaimsService) {
        return new SecurityEndpoint(tokenStoreService, securityClaimsService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncryptService passwordEncryptService() {
        return new PasswordEncryptServiceImpl();
    }
}
