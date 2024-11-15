package ai.llmchat.common.langchain.rag.output;

import dev.langchain4j.model.output.structured.Description;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Prompt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Description("Task Objective")
    private String taskObjective;
    @Description("Output Format")
    private String outputFormat;
    @Description("Key Points")
    private String keyPoints;
    @Description("prologue")
    private String prologue;
}
