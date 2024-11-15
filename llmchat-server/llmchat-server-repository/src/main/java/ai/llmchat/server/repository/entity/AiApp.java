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
 * 应用
 * </p>
 *
 * @author lixw
 * @since 2024-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ai_app")
public class AiApp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * LLM模型
     */
    @TableField("model_id")
    private Long modelId;

    /**
     * Rerank模型
     */
    @TableField("rerank_id")
    private Long rerankId;

    /**
     * 应用名称
     */
    @TableField("name")
    private String name;

    /**
     * 应用描述
     */
    @TableField("description")
    private String description;

    /**
     * 展示回答来源:0-否;1-是
     */
    @TableField("source_enabled")
    private Integer sourceEnabled;

    /**
     * 多轮对话改写:0-否;1-是
     */
    @TableField("rewrite_enabled")
    private Integer rewriteEnabled;

    /**
     * 问题建议:0-否;1-是
     */
    @TableField("suggest_enabled")
    private Integer suggestEnabled;

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
     * 温度系数
     */
    @TableField("temperature")
    private BigDecimal temperature;

    /**
     * 最大输出长度
     */
    @TableField("max_tokens")
    private Integer maxTokens;

    /**
     * 携带上下文轮数
     */
    @TableField("max_memory")
    private Integer maxMemory;

    /**
     * 提示词
     */
    @TableField("prompt")
    private String prompt;

    /**
     * 开场白
     */
    @TableField("prologue")
    private String prologue;

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
