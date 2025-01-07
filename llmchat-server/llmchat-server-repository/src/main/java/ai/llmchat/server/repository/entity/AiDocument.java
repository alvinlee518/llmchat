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
import org.apache.ibatis.type.ArrayTypeHandler;

/**
 * <p>
 * 数据文档
 * </p>
 *
 * @author lixw
 * @since 2024-10-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "ai_document", autoResultMap = true)
public class AiDocument implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 数据集Id
	 */
	@TableField("dataset_id")
	private Long datasetId;

	/**
	 * 文件Id
	 */
	@TableField("file_id")
	private Long fileId;

	/**
	 * 文档名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 数据类型:0-非结构;1-结构
	 */
	@TableField("data_type")
	private Integer dataType;

	/**
	 * 分段模式:0-智能切分;1-自定义切分
	 */
	@TableField("segment_mode")
	private Integer segmentMode;

	/**
	 * 分段标识符
	 */
	@TableField(value = "separators", typeHandler = ArrayTypeHandler.class)
	private String[] separators;

	/**
	 * 分段预估长度
	 */
	@TableField("chunk_size")
	private Integer chunkSize;

	/**
	 * 分段重叠长度
	 */
	@TableField("chunk_overlap")
	private Integer chunkOverlap;

	/**
	 * 数据清洗规则
	 */
	@TableField(value = "clean_rules", typeHandler = ArrayTypeHandler.class)
	private Integer[] cleanRules;

	/**
	 * 索引状态:0-待处理;1-处理中;2-已处理;3-处理失败
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 失败原因
	 */
	@TableField("failure")
	private String failure;

	/**
	 * 检索字段(位运算):1-title;2-content;
	 */
	@TableField("embed_cols")
	private Integer embedCols;

	/**
	 * 模型回复字段(位运算):1-title;2-content;
	 */
	@TableField("reply_cols")
	private Integer replyCols;

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
