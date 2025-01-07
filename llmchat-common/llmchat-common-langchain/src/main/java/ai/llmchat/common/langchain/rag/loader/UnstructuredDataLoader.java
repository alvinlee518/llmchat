package ai.llmchat.common.langchain.rag.loader;

import ai.llmchat.common.langchain.document.splitter.DocumentSplitterFactory;
import ai.llmchat.common.langchain.document.transformer.DocumentCleanTransformer;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public class UnstructuredDataLoader {

	public static List<TextSegment> load(String url, String[] separators, Integer size, Integer overlap,
			Integer[] cleanPatterns) {
		try {
			Document document = UrlDocumentLoader.load(url, new ApacheTikaDocumentParser());
			document = new DocumentCleanTransformer(cleanPatterns).transform(document);
			return DocumentSplitterFactory.create(separators, size, overlap).split(document);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to load document", e);
		}
	}

}
