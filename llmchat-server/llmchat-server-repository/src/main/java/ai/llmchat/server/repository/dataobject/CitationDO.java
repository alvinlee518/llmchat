package ai.llmchat.server.repository.dataobject;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CitationDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String url;
    private List<SegmentDO> segments;
}