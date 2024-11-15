package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParagraphPageParam extends PageParam {
    /**
     * 文档Id
     */
    private Long docId;
    /**
     * 分段名称
     */
    private String keyword;
    /**
     * 索引状态:0-待处理;1-处理中;2-已处理;3-处理失败
     */
    private Integer indexState;
}
