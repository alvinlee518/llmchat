package ai.llmchat.common.langchain.rag.content;

import dev.langchain4j.model.output.TokenUsage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class IngestionResult {

	private final TokenUsage tokenUsage;

	private final List<String> embedIdList;

}
