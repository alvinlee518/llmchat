package ai.llmchat.common.langchain.rag.options;

import ai.llmchat.common.langchain.model.options.EmbeddingModelOptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentOptions {

	private Long id;

	private String name;

	private String description;

	private EmbeddingModelOptions modelOptions;

	private Integer searchMode;

	private Integer topK;

	private Double score;

}
