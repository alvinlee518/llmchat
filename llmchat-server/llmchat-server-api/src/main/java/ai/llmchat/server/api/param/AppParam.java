package ai.llmchat.server.api.param;

import ai.llmchat.server.api.vo.DatasetItemVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AppParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 提示词
	 */
	private String prompt;

	/**
	 * 开场白
	 */
	private String prologue;

	/**
	 * 模型配置
	 */
	private Long modelId;

	private String modelName;

	private BigDecimal temperature;

	private Integer maxTokens;

	private Integer maxMemory;

	private Integer suggestEnabled;

	/**
	 * 数据集配置
	 */
	private Long rerankId;

	private String rerankModelName;

	private Integer sourceEnabled;

	private Integer rewriteEnabled;

	private Integer topK;

	private BigDecimal score;

	private List<DatasetItemVO> datasets;

}
