package ai.llmchat.common.langchain.rag.options;

import ai.llmchat.common.langchain.model.options.LanguageModelOptions;
import ai.llmchat.common.langchain.rag.output.Citation;
import ai.llmchat.common.langchain.rag.output.Message;
import dev.langchain4j.service.tool.ToolProvider;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Setter
public class ChatOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long chatId;
    private String systemPrompt;
    private String instruction;
    private Integer maxMemory;
    private ToolProvider toolProvider;
    private LanguageModelOptions modelOptions;
    private RetrievalOptions retrieval;
    private Function<List<Long>, List<Citation>> citationProvider;
    private Consumer<Message> doOnComplete;
}
