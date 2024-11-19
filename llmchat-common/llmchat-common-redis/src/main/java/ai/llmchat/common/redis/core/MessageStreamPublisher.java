package ai.llmchat.common.redis.core;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MessageStreamPublisher {
    private final StringRedisTemplate stringRedisTemplate;

    public MessageStreamPublisher(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public SendResult publish(Message message) {
        ObjectRecord<String, String> objectRecord = StreamRecords.objectBacked(message.getBody()).withStreamKey(StreamConstants.streamKey(message.getTopic()));
        RecordId recordId = stringRedisTemplate.opsForStream().add(objectRecord);
        message.setSequence(recordId.getSequence());
        message.setTimestamp(recordId.getTimestamp());
        return new SendResult(recordId.getSequence(), recordId.getTimestamp(), recordId.getValue());
    }

    public List<SendResult> publish(@NonNull List<Message> list) {
        List<SendResult> results = new ArrayList<>();
        for (Message message : list) {
            SendResult result = publish(message);
            results.add(result);
        }
        return results;
    }
}
