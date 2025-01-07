package ai.llmchat.common.langchain.rag.content;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.store.embedding.filter.Filter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentSearchOptions {

	private final String keyword;

	private final Embedding embedding;

	private final Integer maxResults;

	private final Double minScore;

	private final Filter filter;

}
