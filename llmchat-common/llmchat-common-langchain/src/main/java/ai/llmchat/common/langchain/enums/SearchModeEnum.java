package ai.llmchat.common.langchain.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

/**
 * 检索模式:0-向量检索;1-全文检索;2-混合检索;
 */
@Getter
public enum SearchModeEnum implements BaseEnum<Integer> {
    SIMILARITY(0, "向量检索"),
    KEYWORD(1, "全文检索"),
    HYBRID(2, "混合检索");
    private final Integer code;
    private final String message;

    SearchModeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
