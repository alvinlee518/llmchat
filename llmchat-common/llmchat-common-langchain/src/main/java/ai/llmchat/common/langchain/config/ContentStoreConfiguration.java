package ai.llmchat.common.langchain.config;

import ai.llmchat.common.langchain.rag.content.ContentStoreIngestor;
import ai.llmchat.common.langchain.rag.memory.RedisChatMemoryStore;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import ai.llmchat.common.langchain.rag.content.weaviate.WeaviateContentStore;
import ai.llmchat.common.langchain.util.LangchainConstants;
import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateAuthClient;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.v1.auth.exception.AuthException;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ContentStoreProperties.class)
public class ContentStoreConfiguration {

	@Bean
	public ContentStore contentStore(ContentStoreProperties properties) throws AuthException {
		Config config = new Config(properties.getScheme(), properties.getHost());
		WeaviateClient client = WeaviateAuthClient.apiKey(config, properties.getApiKey());
		return new WeaviateContentStore(client, ConsistencyLevel.QUORUM, properties.getObjectClass(),
				List.of(LangchainConstants.METADATA_FIELD_DATASET, LangchainConstants.METADATA_FIELD_DOCUMENT,
						LangchainConstants.METADATA_FIELD_PARAGRAPH));
	}

	@Bean
	@ConditionalOnClass(RedisTemplate.class)
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	public RedisTemplate<String, ChatMessage> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, ChatMessage> redisTemplate = new RedisTemplate<>();
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
	@ConditionalOnBean(RedisTemplate.class)
	public ChatMemoryStore chatMemoryStoreWithRedis(RedisTemplate<String, ChatMessage> redisTemplate) {
		return new RedisChatMemoryStore(redisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean
	public ChatMemoryStore chatMemoryStoreWithMemory() {
		return new InMemoryChatMemoryStore();
	}

	@Bean
	@ConditionalOnMissingBean
	public ContentStoreIngestor contentStoreIngestor(ContentStore contentStore) {
		return ContentStoreIngestor.builder().contentStore(contentStore).build();
	}

}
