package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ModelProviderVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private Integer value;

	private String icon;

	private List<SelectOption> supportedModelTypes;

}
