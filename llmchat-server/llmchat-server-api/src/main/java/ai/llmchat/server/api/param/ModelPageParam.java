package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelPageParam extends PageParam {

	private Integer modelProvider;

	private String modelName;

	private Integer status;

}