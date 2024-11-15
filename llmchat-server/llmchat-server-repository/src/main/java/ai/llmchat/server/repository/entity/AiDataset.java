package ai.llmchat.server.repository.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据集
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ai_dataset")
public class AiDataset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 向量模型
     */
    @TableField("embed_id")
    private Long embedId;

    /**
     * 数据集名称
     */
    @TableField("name")
    private String name;

    /**
     * 数据集描述
     */
    @TableField("description")
    private String description;

    /**
     * 检索模式:0-向量检索;1-全文检索;2-混合检索;
     */
    @TableField("search_mode")
    private Integer searchMode;

    /**
     * 召回数量
     */
    @TableField("top_k")
    private Integer topK;

    /**
     * 相似度
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 有效状态：0-无效；1-有效
     */
    @TableField(value = "status", fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_at", fill = FieldFill.INSERT)
    private LocalDateTime createAt;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}
