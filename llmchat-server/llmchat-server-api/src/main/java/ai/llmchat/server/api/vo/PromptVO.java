package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PromptVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String taskObjective;
    private String outputFormat;
    private String keyPoints;
    private String prologue;
}
