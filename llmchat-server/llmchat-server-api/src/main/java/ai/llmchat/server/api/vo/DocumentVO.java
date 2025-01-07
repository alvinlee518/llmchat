package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DocumentVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;

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
	 * 状态:0-待处理;1-处理中;2-已结束;3-错误
	 */
	private Integer state;

	/**
	 * 失败原因
	 */
	private String failure;

	/**
	 * 检索字段(位运算):1-title;2-content;
	 */
	private Integer embedCols;

	/**
	 * 模型回复字段(位运算):1-title;2-content;
	 */
	private Integer replyCols;

}
