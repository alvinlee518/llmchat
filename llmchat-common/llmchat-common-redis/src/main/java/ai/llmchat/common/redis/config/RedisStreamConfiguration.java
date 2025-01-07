package ai.llmchat.common.redis.config;

import ai.llmchat.common.redis.core.MessageStreamConsumer;
import ai.llmchat.common.redis.core.MessageStreamListener;
import ai.llmchat.common.redis.core.MessageStreamPublisher;
import ai.llmchat.common.redis.core.PendingMessageScheduler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling
@Configuration
@AutoConfigureAfter(RedisTemplateConfiguration.class)
@EnableConfigurationProperties(RedisStreamProperties.class)
public class RedisStreamConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MessageStreamPublisher messageStreamPublisher(StringRedisTemplate stringRedisTemplate) {
		return new MessageStreamPublisher(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean
	public MessageStreamConsumer messageStreamConsumer(RedisStreamProperties redisStreamProperties,
			StringRedisTemplate stringRedisTemplate, List<MessageStreamListener> messageStreamListeners) {
		return new MessageStreamConsumer(redisStreamProperties, stringRedisTemplate, messageStreamListeners);
	}

	@Bean
	@ConditionalOnMissingBean
	public PendingMessageScheduler pendingMessageScheduler(RedisStreamProperties redisStreamProperties,
			StringRedisTemplate stringRedisTemplate, List<MessageStreamListener> messageStreamListeners) {
		return new PendingMessageScheduler(redisStreamProperties, stringRedisTemplate, messageStreamListeners);
	}

}
