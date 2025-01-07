package ai.llmchat.common.security;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Optional;

public class SecurityUtils {

	private static final TransmittableThreadLocal<SecurityClaims> THREAD_LOCAL = new TransmittableThreadLocal<>();

	public static Long getId() {
		return Optional.ofNullable(get().getId()).orElse(0L);
	}

	public static String getUsername() {
		return Optional.ofNullable(get().getUsername()).orElse(StringUtils.EMPTY);
	}

	public static String getNickName() {
		return Optional.ofNullable(get().getNickName()).orElse(StringUtils.EMPTY);
	}

	public static SecurityClaims get() {
		return Optional.ofNullable(THREAD_LOCAL.get()).orElse(new SecurityClaims());
	}

	public static void set(SecurityClaims claims) {
		Assert.notNull(claims, "claims can not be null");
		THREAD_LOCAL.set(claims);
	}

	public static void clear() {
		THREAD_LOCAL.remove();
	}

}
