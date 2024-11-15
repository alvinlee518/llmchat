package ai.llmchat.common.langchain.model.options;

import ai.llmchat.common.langchain.enums.ResponseFormatEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageModelOptions extends ModelOptions {
    /**
     * 温度
     */
    private Double temperature;
    /**
     * 上下文窗口大小
     */
    private Integer numCtx;
    /**
     * 最大令牌数预测
     */
    private Integer maxTokens;
    /**
     * 指定响应内容的格式
     */
    private ResponseFormatEnum format;
}
