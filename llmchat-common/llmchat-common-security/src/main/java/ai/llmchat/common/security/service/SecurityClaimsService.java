package ai.llmchat.common.security.service;

import ai.llmchat.common.security.SecurityClaims;

public interface SecurityClaimsService {
    SecurityClaims login(String username, String password);

    SecurityClaims findByUsername(String username);

    void changePassword(String oldPwd, String newPwd);
}
