package ai.llmchat.common.langchain.rag.output;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer promptTokens;

	private Integer completionTokens;

	private Integer totalTokens;

	private Long duration;

}