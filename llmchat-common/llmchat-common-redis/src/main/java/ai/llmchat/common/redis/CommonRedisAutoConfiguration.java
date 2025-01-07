package ai.llmchat.common.redis;

import ai.llmchat.common.redis.config.RedisStreamConfiguration;
import ai.llmchat.common.redis.config.RedisTemplateConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({ RedisTemplateConfiguration.class, RedisStreamConfiguration.class, })
public class CommonRedisAutoConfiguration {

}
