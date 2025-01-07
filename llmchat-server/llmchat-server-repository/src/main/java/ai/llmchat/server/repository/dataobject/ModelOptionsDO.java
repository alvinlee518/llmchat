package ai.llmchat.server.repository.dataobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ModelOptionsDO implements Serializable {

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
	 * 模型类型
	 */
	private Integer modelType;

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
	 * 温度
	 */
	private Double temperature;

	/**
	 * 上下文窗口大小
	 */
	private Integer numCtx;

	/**
	 * 最大令牌数预测
	 */
	private Integer maxTokens;

	/**
	 * 指定响应内容的格式
	 */
	private String format;

}
