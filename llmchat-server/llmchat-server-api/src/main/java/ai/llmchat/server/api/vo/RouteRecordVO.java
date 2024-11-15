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
public class RouteRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String path;
    private String component;
    private RouteMetaVO meta;
    private List<RouteRecordVO> children;
}
