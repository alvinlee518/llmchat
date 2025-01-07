package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ModelVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 模型名称
	 */
	private String modelName;

	/**
	 * 模型提供商
	 */
	private Integer modelProvider;

	/**
	 *
	 */
	private String modelProviderName;

	/**
	 *
	 */
	private String modelProviderIcon;

	/**
	 * 模型类型
	 */
	private Integer modelType;

	/**
	 *
	 */
	private String modelTypeName;

	/**
	 * API域名
	 */
	private String baseUrl;

	/**
	 * API Key
	 */
	private String apiKey;

	/**
	 * Secret Key
	 */
	private String secretKey;

	/**
	 * 有效状态：0-无效；1-有效
	 */
	private Integer status;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateAt;

	/**
	 * 更新人
	 */
	private String updateBy;

}
