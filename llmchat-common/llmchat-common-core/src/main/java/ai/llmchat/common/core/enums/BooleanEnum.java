package ai.llmchat.common.core.enums;

import lombok.Getter;

@Getter
public enum BooleanEnum implements BaseEnum<Integer> {
    YES(1, "是"), NO(0, "否");
    private final Integer code;
    private final String message;

    BooleanEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
