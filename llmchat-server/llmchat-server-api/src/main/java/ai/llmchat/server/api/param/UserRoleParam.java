package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserRoleParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Long> userIds;
    private Long roleId;
}
