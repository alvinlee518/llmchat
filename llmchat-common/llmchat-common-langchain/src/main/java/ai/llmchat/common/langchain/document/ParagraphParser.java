package ai.llmchat.common.langchain.document;

import java.util.List;

public interface ParagraphParser {
    List<Paragraph> parse();
}
