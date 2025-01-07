package ai.llmchat.common.langchain.rag.loader;

import dev.langchain4j.data.segment.TextSegment;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
public class TextSegmentLoader {

	public static final String PROMPT = "prompt";

	public static final String INDEX = "index";

	private final Integer dataType;

	private final String url;

	private final Integer size;

	private final Integer overlap;

	private final String[] separators;

	private final Integer[] cleanPatterns;

	public List<TextSegment> load() {
		List<TextSegment> segments;
		if (Objects.equals(1, dataType)) {
			segments = StructuredDataLoader.load(url);
		}
		else {
			segments = UnstructuredDataLoader.load(url, separators, size, overlap, cleanPatterns);
		}
		return segments;
	}

}
