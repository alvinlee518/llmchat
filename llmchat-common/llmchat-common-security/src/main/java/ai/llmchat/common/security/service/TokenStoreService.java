package ai.llmchat.common.security.service;

import ai.llmchat.common.security.SecurityClaims;

public interface TokenStoreService {
    String store(SecurityClaims claims);

    SecurityClaims read(String token);

    String readAndRefresh(String token);

    void remove(String token);
}
