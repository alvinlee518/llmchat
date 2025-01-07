package ai.llmchat.common.redis.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String topic;

	private String body;

	@Nullable
	private Long sequence;

	private Long timestamp;

	public Message(String topic, String body) {
		this.topic = topic;
		this.body = body;
	}

	public static Message of(String topic, Long body) {
		return new Message(topic, String.valueOf(body));
	}

}
