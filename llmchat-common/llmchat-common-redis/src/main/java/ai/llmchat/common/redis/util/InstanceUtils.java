package ai.llmchat.common.redis.util;

import ai.llmchat.common.core.util.HostUtils;

import java.util.Objects;

public class InstanceUtils {
    public static String getInstanceName(Integer index) {
        return HostUtils.getInetAddress() + "#" + HostUtils.getPid() + "#" + System.nanoTime() + "#" + (Objects.isNull(index) ? 0 : index);
    }
}
