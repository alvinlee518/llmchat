package ai.llmchat.common.langchain.model.options;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageModelOptions extends ModelOptions {
    private String size;
    private String quality;
    private String style;
}
