package ai.llmchat.server.api.param;

import ai.llmchat.server.api.vo.ChatVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AppChatVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 问题建议
	 */
	private Integer suggestEnabled;

	/**
	 * 开场白
	 */
	private String prologue;

	private List<ChatVO> chatList;

}
