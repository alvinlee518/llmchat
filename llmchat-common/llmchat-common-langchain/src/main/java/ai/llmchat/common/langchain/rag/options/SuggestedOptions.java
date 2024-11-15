package ai.llmchat.common.langchain.rag.options;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SuggestedOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long chatId;
    private String modelName;
    private Integer modelProvider;
    private Integer modelType;
    private String baseUrl;
    private String apiKey;
    private String secretKey;
}
