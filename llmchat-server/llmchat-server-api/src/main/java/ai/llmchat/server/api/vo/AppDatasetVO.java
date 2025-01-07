package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppDatasetVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	/**
	 * 数据集名称
	 */
	private String name;

	/**
	 * 数据集描述
	 */
	private String description;

	/**
	 * 文档数量
	 */
	private Integer docCount;

	/**
	 * 是否选中
	 */
	private Integer selected;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateAt;

}
