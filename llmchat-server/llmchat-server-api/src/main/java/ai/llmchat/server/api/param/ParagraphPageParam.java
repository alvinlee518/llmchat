package ai.llmchat.server.api.param;

import ai.llmchat.common.core.wrapper.param.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParagraphPageParam extends PageParam {

	/**
	 * 文档Id
	 */
	private Long docId;

	/**
	 * 分段名称
	 */
	private String keyword;

	/**
	 * 状态:0-待处理;1-处理中;2-已结束;3-错误
	 */
	private Integer state;

}
