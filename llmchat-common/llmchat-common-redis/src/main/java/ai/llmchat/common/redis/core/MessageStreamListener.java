package ai.llmchat.common.redis.core;

public interface MessageStreamListener {

    Action doConsume(Message message);

    String getGroup();

    String getTopic();
}
