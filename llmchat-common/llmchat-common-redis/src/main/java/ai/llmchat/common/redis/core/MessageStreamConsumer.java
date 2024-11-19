package ai.llmchat.common.redis.core;

import ai.llmchat.common.redis.config.RedisStreamProperties;
import ai.llmchat.common.redis.util.InstanceUtils;
import cn.hutool.core.thread.NamedThreadFactory;
import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MessageStreamConsumer implements InitializingBean, DisposableBean {

    private final RedisStreamProperties properties;
    private final StringRedisTemplate stringRedisTemplate;
    private final List<MessageStreamListener> messageStreamListeners;
    private final List<StreamMessageListenerContainer<String, ObjectRecord<String, String>>> containerList = new ArrayList<>();

    public MessageStreamConsumer(RedisStreamProperties properties, StringRedisTemplate stringRedisTemplate, List<MessageStreamListener> messageStreamListeners) {
        this.properties = properties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.messageStreamListeners = messageStreamListeners;
    }

    @Override
    public void destroy() throws Exception {
        for (StreamMessageListenerContainer<String, ObjectRecord<String, String>> container : containerList) {
            container.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (MessageStreamListener messageStreamListener : messageStreamListeners) {
            // 创建消费组
            createGroup(messageStreamListener);
            // 配置 Redis Stream 消费监听容器
            createContainer(messageStreamListener);
        }
    }

    private void createContainer(MessageStreamListener streamListener) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(properties.getPollTimeout())
                .batchSize(properties.getMessagesPerPoll())
                .targetType(String.class)
                .executor(createThreadPool(streamListener.getGroup()))
                .build();
        try {
            StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                    .create(Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()), options);
            String streamName = StreamConstants.streamKey(streamListener.getTopic());
            for (int i = 0; i < properties.getConsumerCount(); i++) {
                registerConsumer(container, streamListener, streamName, i);
            }
            container.start();
            containerList.add(container);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void createGroup(MessageStreamListener streamListener) {
        String streamName = StreamConstants.streamKey(streamListener.getTopic());
        String groupName = streamListener.getGroup();
        try {
            stringRedisTemplate.opsForStream().createGroup(streamName, groupName);
        } catch (RedisSystemException e) {
            if (e.getRootCause() instanceof RedisBusyException) {
                log.info("STREAM - Redis group already exists, skipping Redis group creation: {}", groupName);
            } else if (e.getRootCause() instanceof RedisCommandExecutionException) {
                log.info("STREAM - Stream does not yet exist, creating an empty stream: {}", streamName);
                // 创建流并创建消费组
                stringRedisTemplate.opsForStream().add(StreamRecords.objectBacked(StringUtils.EMPTY).withStreamKey(streamName));
                stringRedisTemplate.opsForStream().createGroup(streamName, groupName);
            } else {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    private void registerConsumer(StreamMessageListenerContainer<String, ObjectRecord<String, String>> container,
                                  MessageStreamListener streamListener, String streamName, int index) {
        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = StreamMessageListenerContainer
                .StreamReadRequest.builder(StreamOffset.create(streamName, ReadOffset.lastConsumed()))
                .errorHandler(throwable -> log.error("Error during message processing: {}", throwable.getMessage(), throwable))
                .cancelOnError(e -> false)
                .consumer(Consumer.from(streamListener.getGroup(), InstanceUtils.getInstanceName(index)))
                .autoAcknowledge(false)
                .build();

        container.register(streamReadRequest, message -> processMessage(streamListener, message));
    }

    private void processMessage(MessageStreamListener streamListener, ObjectRecord<String, String> message) {
        if (StringUtils.isBlank(message.getValue())) {
            stringRedisTemplate.opsForStream().acknowledge(streamListener.getGroup(), message);
            return;
        }

        RecordId recordId = message.getId();
        Message msg = new Message(streamListener.getTopic(), message.getValue());
        msg.setSequence(recordId.getSequence());
        msg.setTimestamp(recordId.getTimestamp());

        try {
            Action action = streamListener.doConsume(msg);
            if (Action.CommitMessage.equals(action)) {
                stringRedisTemplate.opsForStream().acknowledge(streamListener.getGroup(), message);
            }
        } catch (Exception e) {
            log.error("Message processing failed: {}", e.getMessage(), e);
        }
    }

    private ThreadPoolExecutor createThreadPool(String groupName) {
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(properties.getQueueCapacity()),
                new NamedThreadFactory(groupName + "-", false),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
