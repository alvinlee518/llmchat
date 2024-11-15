package ai.llmchat.common.security.service.impl;

import ai.llmchat.common.security.service.PasswordEncryptService;
import cn.hutool.crypto.SecureUtil;

public class PasswordEncryptServiceImpl implements PasswordEncryptService {
    @Override
    public String encrypt(String password) {
        return SecureUtil.sha256(password);
    }
}
