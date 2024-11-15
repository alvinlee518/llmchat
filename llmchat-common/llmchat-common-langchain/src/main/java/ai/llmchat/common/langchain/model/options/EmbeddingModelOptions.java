package ai.llmchat.common.langchain.model.options;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbeddingModelOptions extends ModelOptions {
    private Integer dimensions;
}
