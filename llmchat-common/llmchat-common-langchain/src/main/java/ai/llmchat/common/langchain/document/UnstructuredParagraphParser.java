package ai.llmchat.common.langchain.document;

import ai.llmchat.common.langchain.document.splitter.DocumentSplitterFactory;
import ai.llmchat.common.langchain.document.transformer.DocumentCleanTransformer;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UnstructuredParagraphParser implements ParagraphParser {
    private final String url;
    private final String[] separators;
    private final Integer chunkSize;
    private final Integer chunkOverlap;
    private final Integer[] cleanPatterns;

    public UnstructuredParagraphParser(String url,
                                       String[] separators,
                                       Integer chunkSize,
                                       Integer chunkOverlap,
                                       Integer[] cleanPatterns
    ) {
        this.url = url;
        this.separators = separators;
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.cleanPatterns = cleanPatterns;
    }

    @Override
    public List<Paragraph> parse() {
        try {
            Document document = UrlDocumentLoader.load(url, new ApacheTikaDocumentParser());
            document = new DocumentCleanTransformer(cleanPatterns).transform(document);
            List<TextSegment> segments = DocumentSplitterFactory.create(separators, chunkSize, chunkOverlap).split(document);
            return segments.stream().map(item -> new Paragraph(
                            StringUtils.EMPTY,
                            item.text(),
                            item.metadata().getInteger("index")
                    )
            ).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load document", e);
        }
    }
}
