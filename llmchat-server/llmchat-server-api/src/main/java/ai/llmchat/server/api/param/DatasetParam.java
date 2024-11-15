package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class DatasetParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long id;
    /**
     * 向量模型
     */
    private Long embedId;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 数据集描述
     */
    private String description;

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
    private BigDecimal score;
}
