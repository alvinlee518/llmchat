package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppDatasetParam extends PageParam {
    private String keyword;
    private Long appId;
}
