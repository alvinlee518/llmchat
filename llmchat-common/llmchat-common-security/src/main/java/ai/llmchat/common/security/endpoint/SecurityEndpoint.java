package ai.llmchat.common.security.endpoint;

import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.security.SecurityClaims;
import ai.llmchat.common.security.service.SecurityClaimsService;
import ai.llmchat.common.security.service.TokenStoreService;
import ai.llmchat.common.security.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityEndpoint {
    private final TokenStoreService tokenStoreService;
    private final SecurityClaimsService securityClaimsService;

    public SecurityEndpoint(TokenStoreService tokenStoreService, SecurityClaimsService securityClaimsService) {
        this.tokenStoreService = tokenStoreService;
        this.securityClaimsService = securityClaimsService;
    }

    @PostMapping("/security/login")
    public Result<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        SecurityClaims claims = securityClaimsService.login(username, password);
        String token = tokenStoreService.store(claims);
        return Result.data(token);
    }

    @GetMapping("/security/logout")
    public Result<?> logout(HttpServletRequest request) {
        String token = RequestUtils.getToken(request);
        tokenStoreService.remove(token);
        return Result.success();
    }

    @PostMapping("/account/change_password")
    public Result<?> changePassword(HttpServletRequest request, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword) {
        securityClaimsService.changePassword(password, newPassword);
        String token = RequestUtils.getToken(request);
        tokenStoreService.remove(token);
        return Result.success();
    }
}
