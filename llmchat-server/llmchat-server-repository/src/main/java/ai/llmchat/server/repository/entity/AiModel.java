package ai.llmchat.server.repository.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 模型配置
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ai_model")
public class AiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 模型名称
	 */
	@TableField("model_name")
	private String modelName;

	/**
	 * 模型提供商
	 */
	@TableField("model_provider")
	private Integer modelProvider;

	/**
	 * 模型类型
	 */
	@TableField("model_type")
	private Integer modelType;

	/**
	 * API域名
	 */
	@TableField("base_url")
	private String baseUrl;

	/**
	 * API Key
	 */
	@TableField("api_key")
	private String apiKey;

	/**
	 * Secret Key
	 */
	@TableField("secret_key")
	private String secretKey;

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
