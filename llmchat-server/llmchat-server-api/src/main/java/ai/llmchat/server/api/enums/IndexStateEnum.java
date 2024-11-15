package ai.llmchat.server.api.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum IndexStateEnum implements BaseEnum<Integer> {
    PENDING(0, "待解析"),
    IN_PROCESSING(1, "解析中"),
    COMPLETION(2, "解析完成"),
    FAILURE(3, "解析失败");;
    private final Integer code;
    private final String message;

    IndexStateEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
