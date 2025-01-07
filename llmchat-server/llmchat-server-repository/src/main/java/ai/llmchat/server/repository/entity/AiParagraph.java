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
 * 数据文档
 * </p>
 *
 * @author lixw
 * @since 2024-11-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ai_paragraph")
public class AiParagraph implements Serializable {

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
	 * 文档Id
	 */
	@TableField("doc_id")
	private Long docId;

	/**
	 * 索引Id
	 */
	@TableField("index_id")
	private String indexId;

	/**
	 * 分段标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 分段内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 分段位置
	 */
	@TableField("position")
	private Integer position;

	/**
	 * 字符数
	 */
	@TableField("word_count")
	private Integer wordCount;

	/**
	 * 命中次数
	 */
	@TableField("hit_count")
	private Integer hitCount;

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
