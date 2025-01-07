package ai.llmchat.common.core.wrapper.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SelectGroupNode implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private Long value;

	private String type;

	private List<SelectGroupNode> children;

	public SelectGroupNode() {
	}

	public SelectGroupNode(String label, Long value) {
		this.label = label;
		this.value = value;
	}

}
