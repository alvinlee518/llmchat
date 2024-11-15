package ai.llmchat.common.core.enums;

import lombok.Getter;

@Getter
public enum StatusEnum implements BaseEnum<Integer> {
    INVALID(0, "无效"), VALID(1, "有效");
    private final Integer code;
    private final String message;

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
