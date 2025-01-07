package ai.llmchat.common.langchain.model.options;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;

@Getter
@Setter
public class ModelOptions implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer modelProvider;

	private String modelName;

	private Integer modelType;

	private String baseUrl;

	private String apiKey;

	private String secretKey;

	private Duration timeout;

	private Integer maxRetries;

	private Boolean logRequests;

	private Boolean logResponses;

}
