package ai.llmchat.common.redis.core;

public interface StreamConstants {
    String KEY_PREFIX = "stream:";
    String FAILURE_LIST_KEY = "stream:failure_list";
    Integer MAX_RETRY = 3;
    Integer CPU_NUM = Runtime.getRuntime().availableProcessors();

    static String streamKey(String key) {
        return KEY_PREFIX + key;
    }
}
