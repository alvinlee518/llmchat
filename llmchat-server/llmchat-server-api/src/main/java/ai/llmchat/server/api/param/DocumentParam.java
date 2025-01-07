package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DocumentParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Long id;

	/**
	 * 数据集Id
	 */
	private Long datasetId;

	/**
	 * 文件Id
	 */
	private Long fileId;

	/**
	 * 文档名称
	 */
	private String name;

	/**
	 * 数据类型:0-非结构;1-结构
	 */
	private Integer dataType;

	/**
	 * 分段模式:0-智能切分;1-自定义切分
	 */
	private Integer segmentMode;

	/**
	 * 分段标识符
	 */
	private String[] separators;

	/**
	 * 分段预估长度
	 */
	private Integer chunkSize;

	/**
	 * 分段重叠长度
	 */
	private Integer chunkOverlap;

	/**
	 * 数据清洗规则
	 */
	private Integer[] cleanRules;

	/**
	 * 检索字段(位运算):1-title;2-content;
	 */
	private Integer embedCols;

	/**
	 * 模型回复字段(位运算):1-title;2-content;
	 */
	private Integer replyCols;

}
