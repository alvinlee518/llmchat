package ai.llmchat.server.api.vo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitTestingVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 分段标题
     */
    private String title;

    /**
     * 分段内容
     */
    private String content;

    /**
     * score
     */
    private Double score;
}
