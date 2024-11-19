package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentItemVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long id;
    /**
     * 文档名称
     */
    private String name;

    /**
     * 分段数
     */
    private Integer paraCount;

    /**
     * 索引数量
     */
    private Integer indexCount;

    /**
     * 状态:0-待处理;1-处理中;2-已结束;3-错误
     */
    private Integer state;

    /**
     * 失败原因
     */
    private String failure;

    /**
     * 更新时间
     */
    private LocalDateTime updateAt;
}
