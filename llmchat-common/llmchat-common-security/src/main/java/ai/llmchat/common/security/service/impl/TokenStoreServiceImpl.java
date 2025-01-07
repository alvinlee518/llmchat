package ai.llmchat.common.security.service.impl;

import ai.llmchat.common.core.exception.AuthenticationException;
import ai.llmchat.common.security.SecurityClaims;
import ai.llmchat.common.security.service.TokenStoreService;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TokenStoreServiceImpl implements TokenStoreService {

	private static final String TOKEN_KEY_PREFIX = "token:";

	private final RedisTemplate<String, SecurityClaims> redisTemplate;

	public TokenStoreServiceImpl(RedisTemplate<String, SecurityClaims> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String store(SecurityClaims claims) {
		Assert.notNull(claims, "claims can not be null");
		String token = IdUtil.fastUUID();
		String key = formatKey(token);
		redisTemplate.opsForValue().set(key, claims, 7, TimeUnit.DAYS);
		return token;
	}

	@Override
	public SecurityClaims read(String token) {
		Assert.notNull(token, "token can not be empty");
		String key = formatKey(token);
		SecurityClaims claims = redisTemplate.opsForValue().get(key);
		if (Optional.ofNullable(claims).map(SecurityClaims::getId).orElse(0L) <= 0) {
			throw new AuthenticationException("Token已过期，请重新登录");
		}
		return claims;
	}

	@Override
	public String readAndRefresh(String token) {
		Assert.notNull(token, "token can not be empty");
		String key = formatKey(token);
		SecurityClaims claims = redisTemplate.opsForValue().getAndExpire(key, 7, TimeUnit.DAYS);
		if (Optional.ofNullable(claims).map(SecurityClaims::getId).orElse(0L) <= 0) {
			throw new AuthenticationException("Token已过期，请重新登录");
		}
		return claims.getUsername();
	}

	@Override
	public void remove(String token) {
		String key = formatKey(token);
		redisTemplate.delete(key);
	}

	private String formatKey(String token) {
		return TOKEN_KEY_PREFIX + token;
	}

}
