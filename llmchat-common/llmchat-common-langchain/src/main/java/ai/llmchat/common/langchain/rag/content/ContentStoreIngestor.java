package ai.llmchat.common.langchain.rag.content;

import ai.llmchat.common.langchain.util.LangchainConstants;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.filter.comparison.IsIn;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
@Builder
public class ContentStoreIngestor {

	private final ContentStore contentStore;

	public IngestionResult ingest(EmbeddingModel embeddingModel, List<TextSegment> segments) {
		List<Long> idList = segments.stream()
			.map(item -> item.metadata().getLong(LangchainConstants.METADATA_FIELD_PARAGRAPH))
			.filter(Objects::nonNull)
			.toList();
		contentStore.removeAll(new IsIn(LangchainConstants.METADATA_FIELD_PARAGRAPH, idList));
		Response<List<Embedding>> response = embeddingModel.embedAll(segments);
		List<String> embedIdList = contentStore.addAll(response.content(), segments);
		return IngestionResult.builder().tokenUsage(response.tokenUsage()).embedIdList(embedIdList).build();
	}

}
