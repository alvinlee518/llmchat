package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthUserPageParam extends PageParam {
    private String name;
    private String email;
    private String phone;
    private Integer status;
    private Long deptId;
}
