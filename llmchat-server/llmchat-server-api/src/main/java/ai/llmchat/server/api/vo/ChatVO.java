package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long chatId;

	private Long appId;

	private String title;

}
