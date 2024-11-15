package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class HitTestingParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 数据集Id
     */
    private Long datasetId;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 检索模式:0-向量检索;1-全文检索;2-混合检索;
     */
    private Integer searchMode;

    /**
     * 召回数量
     */
    private Integer topK;

    /**
     * 相似度
     */
    private Double score;
}
