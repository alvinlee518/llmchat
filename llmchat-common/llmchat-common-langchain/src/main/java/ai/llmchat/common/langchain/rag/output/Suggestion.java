package ai.llmchat.common.langchain.rag.output;

import dev.langchain4j.model.output.structured.Description;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Suggestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Description("question array")
    private List<String> questions;
}
