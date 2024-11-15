package ai.llmchat.common.langchain.rag.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisChatMemoryStore implements ChatMemoryStore {
    private static final String MEMORY_KEY_PREFIX = "chat_memory:";
    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public RedisChatMemoryStore(RedisTemplate<String, ChatMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return redisTemplate.opsForList().range(MEMORY_KEY_PREFIX + memoryId, 0, -1);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        redisTemplate.opsForList().rightPushAll(formatKey(memoryId), messages);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(formatKey(memoryId));
    }

    private String formatKey(Object memoryId) {
        return MEMORY_KEY_PREFIX + String.valueOf(memoryId);
    }
}
