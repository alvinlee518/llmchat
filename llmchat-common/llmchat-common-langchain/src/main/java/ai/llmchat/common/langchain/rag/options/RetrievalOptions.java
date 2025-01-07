package ai.llmchat.common.langchain.rag.options;

import ai.llmchat.common.langchain.model.options.ScoringModelOptions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RetrievalOptions {

	private Boolean rewriteEnabled;

	private ScoringModelOptions modelOptions;

	private Double score;

	private Integer topK;

	private List<ContentOptions> contents;

}
