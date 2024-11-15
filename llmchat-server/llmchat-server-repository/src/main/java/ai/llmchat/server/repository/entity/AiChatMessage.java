package ai.llmchat.server.repository.entity;

import ai.llmchat.server.repository.dataobject.CitationDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 对话
 * </p>
 *
 * @author lixw
 * @since 2024-11-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "ai_chat_message", autoResultMap = true)
public class AiChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 对话Id
     */
    @TableField("chat_id")
    private Long chatId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 指令
     */
    @TableField("instruction")
    private String instruction;

    /**
     * 模型输入
     */
    @TableField("prompt")
    private String prompt;

    /**
     * 模型输入消耗tokens
     */
    @TableField("prompt_tokens")
    private Integer promptTokens;

    /**
     * 模型输出
     */
    @TableField("completion")
    private String completion;

    /**
     * 模型输出消耗tokens
     */
    @TableField("completion_tokens")
    private Integer completionTokens;

    /**
     * 是否异常:0-否;1-是
     */
    @TableField("failed")
    private Integer failed;

    /**
     * 引文
     */
    @TableField(value = "citations", typeHandler = JacksonTypeHandler.class)
    private List<CitationDO> citations;

    /**
     * 耗时(ms)
     */
    @TableField("duration")
    private Long duration;

    /**
     * 有效状态：0-未投票；1-赞成；2-不赞成
     */
    @TableField("rating")
    private Integer rating;

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
