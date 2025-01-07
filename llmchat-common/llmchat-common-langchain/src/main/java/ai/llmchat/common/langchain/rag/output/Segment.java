package ai.llmchat.common.langchain.rag.output;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Segment implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer index;

	private String text;

}