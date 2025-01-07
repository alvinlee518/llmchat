package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OptimizeParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private String instruction;

	private Long modelId;

}
