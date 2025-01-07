package ai.llmchat.common.langchain.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paragraph {

	private String prompt;

	private String completion;

	private Integer position;

}
