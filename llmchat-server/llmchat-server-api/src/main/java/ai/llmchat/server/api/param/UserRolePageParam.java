package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRolePageParam extends PageParam {

	private String name;

	private String phone;

	private Integer userScope;

	private Long roleId;

}
