package ai.llmchat.common.security;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityClaims implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String nickName;

}
