package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EnabledParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer enabled;
}
