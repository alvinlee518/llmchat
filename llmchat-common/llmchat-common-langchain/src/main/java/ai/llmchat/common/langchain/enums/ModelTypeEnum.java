package ai.llmchat.common.langchain.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ModelTypeEnum implements BaseEnum<Integer> {
    LLM(1, "LLM"),
    EMBEDDING(2, "Embedding"),
    RERANK(3, "Rerank"),
    IMAGE(4, "Image"),
    MODERATION(5, "Moderation"),
//    AUDIO(6, "Audio"),
//    VIDEO(7, "Video"),
    ;
    private final Integer code;
    private final String message;

    ModelTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ModelTypeEnum valueOf(Integer value) {
        for (ModelTypeEnum item : values()) {
            if (Objects.equals(value, item.getCode())) {
                return item;
            }
        }
        throw new IllegalArgumentException("No enum constant with code " + value);
    }
}
