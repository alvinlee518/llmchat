package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long chatId;

	private Long appId;

	private String title;

}
