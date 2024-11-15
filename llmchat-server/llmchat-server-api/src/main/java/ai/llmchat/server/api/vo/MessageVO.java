package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long chatId;
    private String role;
    private String content;
    private Boolean error;
    private Integer rating;
    private UsageVO usage;
    private List<CitationVO> citations;
    private LocalDateTime createAt;
}
