package ai.llmchat.server.api.vo;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private Long duration;
}