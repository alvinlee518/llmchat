package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ChatTestingParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long chatId;

	private Long appId;

	private String prompt;

	private String instruction;

	private Long modelId;

	private Double temperature;

	private Integer maxTokens;

	private Integer maxMemory;

	private Integer sourceEnabled;

	private Long rerankId;

	private Integer rewriteEnabled;

	private Integer topK;

	private Double score;

	private List<Long> datasets;

}
