package ai.llmchat.server.repository.dataobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ParagraphDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long docId;
    private String docName;
    private String docUrl;
    private Integer replyCols;
    private Long id;
    private Integer position;
    private String title;
    private String content;
}
