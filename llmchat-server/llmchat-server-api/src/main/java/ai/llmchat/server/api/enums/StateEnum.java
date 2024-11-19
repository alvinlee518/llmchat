package ai.llmchat.server.api.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum StateEnum implements BaseEnum<Integer> {
    PENDING(0, "待处理"),
    IN_PROCESSING(1, "处理中"),
    COMPLETION(2, "已结束"),
    FAILURE(3, "失败");
    private final Integer code;
    private final String message;

    StateEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
