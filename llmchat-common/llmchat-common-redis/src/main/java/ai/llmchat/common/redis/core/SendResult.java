package ai.llmchat.common.redis.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Nullable
	private Long sequence;

	private Long timestamp;

	private String value;

}
