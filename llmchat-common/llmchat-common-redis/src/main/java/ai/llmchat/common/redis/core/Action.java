package ai.llmchat.common.redis.core;

public enum Action {
    CommitMessage,
    ReconsumeLater;

    private Action() {
    }
}
