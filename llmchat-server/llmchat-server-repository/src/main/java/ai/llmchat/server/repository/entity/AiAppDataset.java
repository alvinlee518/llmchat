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
 * 应用数据集关联表
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ai_app_dataset")
public class AiAppDataset implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 应用Id
	 */
	@TableField("app_id")
	private Long appId;

	/**
	 * 数据集Id
	 */
	@TableField("dataset_id")
	private Long datasetId;

	/**
	 * 状态:0-无效;1-有效
	 */
	@TableField(value = "status", fill = FieldFill.INSERT)
	private Integer status;

	/**
	 * 创建人
	 */
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_at", fill = FieldFill.INSERT)
	private LocalDateTime createAt;

	/**
	 * 更新人
	 */
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateAt;

}
