package ai.llmchat.common.langchain.rag.content;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.filter.Filter;

import java.util.List;

public interface ContentStore {
    default List<String> addAll(List<Embedding> embeddings, List<TextSegment> embedded) {
        List<String> ids = embeddings.stream().map(ignored -> Utils.randomUUID()).toList();
        this.addAll(ids, embeddings, embedded);
        return ids;
    }

    void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded);

    void removeAll(Filter filter);

    List<EmbeddingMatch<TextSegment>> keywordSearch(ContentSearchOptions options);

    List<EmbeddingMatch<TextSegment>> similaritySearch(ContentSearchOptions options);

    List<EmbeddingMatch<TextSegment>> hybridSearch(ContentSearchOptions options);
}
