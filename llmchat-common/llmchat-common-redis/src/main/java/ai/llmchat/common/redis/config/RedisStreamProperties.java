package ai.llmchat.common.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = RedisStreamProperties.PREFIX)
public class RedisStreamProperties {

	static final String PREFIX = "spring.data.redis";

	/**
	 * 消息轮询的超时时间。
	 */
	private Duration pollTimeout = Duration.ofSeconds(5);

	/**
	 * 每次轮询的最大消息数。
	 */
	private int messagesPerPoll = 8;

	/**
	 * 消费者数量（每个消费组的消费者实例数）。
	 */
	private int consumerCount = 4;

	/**
	 * 线程池的核心线程数。
	 */
	private int corePoolSize = Runtime.getRuntime().availableProcessors();

	/**
	 * 线程池的最大线程数。
	 */
	private int maxPoolSize = corePoolSize * 2 + 10;

	/**
	 * 线程池的队列容量。
	 */
	private int queueCapacity = 256;

	/**
	 * 线程池空闲线程的最大存活时间。
	 */
	private long keepAliveTime = 60;

}
