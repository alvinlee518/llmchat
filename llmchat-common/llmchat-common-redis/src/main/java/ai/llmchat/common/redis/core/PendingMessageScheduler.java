package ai.llmchat.common.redis.core;

import ai.llmchat.common.redis.config.RedisStreamProperties;
import ai.llmchat.common.redis.util.InstanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PendingMessageScheduler implements InitializingBean {
    private final RedisStreamProperties properties;
    private final StringRedisTemplate stringRedisTemplate;
    private final List<MessageStreamListener> messageStreamListeners;

    public PendingMessageScheduler(RedisStreamProperties properties, StringRedisTemplate stringRedisTemplate, List<MessageStreamListener> messageStreamListeners) {
        this.properties = properties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.messageStreamListeners = messageStreamListeners;
    }

    @Scheduled(fixedDelay = 5, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void processPendingMessage() {
        String instanceName = InstanceUtils.getInstanceName(RandomUtils.secure().randomInt(0, properties.getConsumerCount()));
        for (MessageStreamListener streamListener : messageStreamListeners) {
            String streamName = StreamConstants.streamKey(streamListener.getTopic());
            String groupName = streamListener.getGroup();
            try {

                PendingMessages messages = stringRedisTemplate.opsForStream().pending(streamName, groupName, Range.unbounded(), Integer.MAX_VALUE);
                if (messages.isEmpty()) {
                    log.info("No pending messages for stream: {}, group: {}", streamName, groupName);
                    continue;
                }
                log.info("Found {} pending messages for stream: {}, group: {}", messages.size(), streamName, groupName);
                processMessages(messages, streamListener, streamName, groupName, instanceName);
            } catch (Exception e) {
                log.error("Error processing pending messages for stream: {}, group: {}. Reason: {}", streamName, groupName, e.getMessage(), e);
            }
        }
    }

    private void processMessages(PendingMessages pendingMessages,
                                 MessageStreamListener streamListener,
                                 String streamName,
                                 String groupName,
                                 String instanceName) {
        pendingMessages.stream().parallel()
                .forEach(pendingMessage -> {
                    try {
                        List<MapRecord<String, String, String>> claimedMessages = stringRedisTemplate.<String, String>opsForStream().claim(streamName, groupName, instanceName, Duration.ofMinutes(5), pendingMessage.getId());
                        if (CollectionUtils.isNotEmpty(claimedMessages)) {
                            handleMessage(claimedMessages.get(0), pendingMessage, streamListener, streamName, groupName);
                        }
                    } catch (Exception e) {
                        log.error("Error handling pending message: {}. Reason: {}", pendingMessage.getIdAsString(), e.getMessage(), e);
                    }
                });
    }

    private void handleMessage(MapRecord<String, String, String> record,
                               PendingMessage pendingMessage,
                               MessageStreamListener streamListener,
                               String streamName,
                               String groupName) {
        String payload = record.getValue().getOrDefault("payload", StringUtils.EMPTY);
        String messageId = pendingMessage.getIdAsString();
        if (StringUtils.isBlank(payload)) {
            stringRedisTemplate.opsForStream().acknowledge(streamName, groupName, messageId);
            return;
        }
        if (pendingMessage.getTotalDeliveryCount() > StreamConstants.MAX_RETRY) {
            stringRedisTemplate.opsForList().rightPush(StreamConstants.FAILURE_LIST_KEY, payload);
            stringRedisTemplate.opsForStream().acknowledge(streamName, groupName, messageId);
            log.warn("Message {} moved to failure list after exceeding max retries", messageId);
        } else {
            RecordId recordId = pendingMessage.getId();
            Message msg = new Message(streamListener.getTopic(), payload);
            msg.setSequence(recordId.getSequence());
            msg.setTimestamp(recordId.getTimestamp());

            try {
                Action action = streamListener.doConsume(msg);
                if (Action.CommitMessage.equals(action)) {
                    stringRedisTemplate.opsForStream().acknowledge(streamName, groupName, messageId);
                }
            } catch (Exception e) {
                log.error("Error consuming message: {}. Reason: {}", recordId.getValue(), e.getMessage(), e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
