package ai.llmchat.common.security.handler;

import ai.llmchat.common.core.exception.AuthenticationException;
import ai.llmchat.common.security.SecurityClaims;
import ai.llmchat.common.security.SecurityUtils;
import ai.llmchat.common.security.service.SecurityClaimsService;
import ai.llmchat.common.security.service.TokenStoreService;
import ai.llmchat.common.security.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@Slf4j
public class TokenSecurityInterceptor implements AsyncHandlerInterceptor {
    private final TokenStoreService tokenStoreService;
    private final SecurityClaimsService securityClaimsService;

    public TokenSecurityInterceptor(TokenStoreService tokenStoreService, SecurityClaimsService securityClaimsService) {
        this.tokenStoreService = tokenStoreService;
        this.securityClaimsService = securityClaimsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler
    ) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (StringUtils.equalsIgnoreCase(request.getMethod(), HttpMethod.OPTIONS.name())) {
            return true;
        }

        try {
            String token = RequestUtils.getToken(request);
            if (StringUtils.isBlank(token)) {
                throw new AuthenticationException("无效的Token，请登录后重试");
            }
            // 验证token
            String username = tokenStoreService.readAndRefresh(token);
            // 验证用户
            SecurityClaims claims = securityClaimsService.findByUsername(username);
            SecurityUtils.set(claims);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AuthenticationException(e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex
    ) throws Exception {
        SecurityUtils.clear();
    }
}
