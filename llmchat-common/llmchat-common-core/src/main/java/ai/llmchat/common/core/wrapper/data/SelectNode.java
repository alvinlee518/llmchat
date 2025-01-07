package ai.llmchat.common.core.wrapper.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectNode implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private Long value;

}
