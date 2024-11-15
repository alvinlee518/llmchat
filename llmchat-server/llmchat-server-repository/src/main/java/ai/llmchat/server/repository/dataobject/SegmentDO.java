package ai.llmchat.server.repository.dataobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SegmentDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer index;
    private String text;
}