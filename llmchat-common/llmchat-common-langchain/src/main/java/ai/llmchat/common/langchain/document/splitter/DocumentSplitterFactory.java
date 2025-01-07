package ai.llmchat.common.langchain.document.splitter;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.DocumentByWordSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import org.apache.commons.lang3.ArrayUtils;

public class DocumentSplitterFactory {

	public static DocumentSplitter create(String[] separators, Integer chunkSize, Integer chunkOverlap) {
		if (ArrayUtils.isNotEmpty(separators)) {
			return new DocumentByRegexSplitter(String.join("|", separators), System.lineSeparator(), chunkSize,
					chunkOverlap, null, new DocumentBySentenceSplitter(chunkSize, chunkOverlap, null,
							new DocumentByWordSplitter(chunkSize, chunkOverlap, null, null)));
		}
		return DocumentSplitters.recursive(256, 64);
	}

}
