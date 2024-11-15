package ai.llmchat.server.api.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteMetaVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private List<String> permissions;
    private Boolean noKeepAlive;
    private Boolean hidden;
    private Boolean affix;
}
