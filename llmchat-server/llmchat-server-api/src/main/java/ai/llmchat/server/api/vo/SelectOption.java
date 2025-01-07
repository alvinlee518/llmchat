package ai.llmchat.server.api.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectOption implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private Integer value;

}
