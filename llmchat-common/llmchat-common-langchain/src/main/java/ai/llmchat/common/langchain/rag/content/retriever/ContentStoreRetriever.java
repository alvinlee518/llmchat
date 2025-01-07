package ai.llmchat.common.langchain.rag.content.retriever;

import ai.llmchat.common.langchain.enums.SearchModeEnum;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import ai.llmchat.common.langchain.rag.content.ContentSearchOptions;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.filter.Filter;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Builder
public class ContentStoreRetriever implements ContentRetriever {

	public static final Function<Query, Integer> DEFAULT_MAX_RESULTS = (query) -> 3;

	public static final Function<Query, Double> DEFAULT_MIN_SCORE = (query) -> 0.5;

	public static final Function<Query, Filter> DEFAULT_FILTER = (query) -> null;

	public static final Function<Query, Integer> DEFAULT_SEARCH_MODE = (query) -> SearchModeEnum.HYBRID.getCode();

	private final EmbeddingModel embeddingModel;

	private final ContentStore contentStore;

	private final Function<Query, Integer> maxResultsProvider;

	private final Function<Query, Double> minScoreProvider;

	private final Function<Query, Filter> filterProvider;

	private final Function<Query, Integer> searchModeProvider;

	public ContentStoreRetriever(EmbeddingModel embeddingModel, ContentStore contentStore) {
		this(embeddingModel, contentStore, null, null, null, null);
	}

	public ContentStoreRetriever(EmbeddingModel embeddingModel, ContentStore contentStore,
			Function<Query, Integer> maxResultsProvider, Function<Query, Double> minScoreProvider,
			Function<Query, Filter> filterProvider, Function<Query, Integer> searchModeProvider) {
		this.embeddingModel = ValidationUtils.ensureNotNull(embeddingModel, "embeddingModel");
		this.contentStore = ValidationUtils.ensureNotNull(contentStore, "contentStore");
		this.maxResultsProvider = Utils.getOrDefault(maxResultsProvider, DEFAULT_MAX_RESULTS);
		this.minScoreProvider = Utils.getOrDefault(minScoreProvider, DEFAULT_MIN_SCORE);
		this.filterProvider = Utils.getOrDefault(filterProvider, DEFAULT_FILTER);
		this.searchModeProvider = Utils.getOrDefault(searchModeProvider, DEFAULT_SEARCH_MODE);
	}

	@Override
	public List<Content> retrieve(Query query) {
		Integer searchMode = searchModeProvider.apply(query);
		ContentSearchOptions.ContentSearchOptionsBuilder optionsBuilder = ContentSearchOptions.builder()
			.keyword(query.text())
			.maxResults(maxResultsProvider.apply(query))
			.minScore(minScoreProvider.apply(query))
			.filter(filterProvider.apply(query));
		List<EmbeddingMatch<TextSegment>> searchResult;
		if (Objects.equals(SearchModeEnum.SIMILARITY.getCode(), searchMode)) {
			Embedding embeddedQuery = embeddingModel.embed(query.text()).content();
			searchResult = contentStore.similaritySearch(optionsBuilder.embedding(embeddedQuery).build());
		}
		else if (Objects.equals(SearchModeEnum.KEYWORD.getCode(), searchMode)) {
			searchResult = contentStore.keywordSearch(optionsBuilder.build());
		}
		else {
			Embedding embeddedQuery = embeddingModel.embed(query.text()).content();
			searchResult = contentStore.hybridSearch(optionsBuilder.embedding(embeddedQuery).build());
		}
		return searchResult.stream().map(EmbeddingMatch::embedded).map(Content::from).toList();
	}

}
