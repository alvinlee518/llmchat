package ai.llmchat.common.langchain.document;

import java.util.Objects;

public class ParagraphParserFactory {

	public static ParagraphParser create(Integer dataType, String url, String[] separators, Integer chunkSize,
			Integer chunkOverlap, Integer[] cleanPatterns) {
		if (Objects.equals(1, dataType)) {
			return new StructuredParagraphParser(url);
		}
		else {
			return new UnstructuredParagraphParser(url, separators, chunkSize, chunkOverlap, cleanPatterns);
		}
	}

}
