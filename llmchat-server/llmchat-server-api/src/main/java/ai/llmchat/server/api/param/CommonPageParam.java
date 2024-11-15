package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPageParam extends PageParam {
    private Long parentId;
    private String name;
    private Integer status;
}
