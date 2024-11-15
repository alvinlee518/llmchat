package ai.llmchat.common.mybatis.exception;

import org.apache.ibatis.executor.ErrorContext;

/**
 * 异常处理类
 *
 * @author lxw
 */
public class ExceptionFactory {
    /**
     * 构建数据权限异常
     *
     * @param message
     * @param e
     * @return
     */
    public static RuntimeException dataScope(String message, Exception e) {
        return new DataScopeException(ErrorContext.instance().message(message).cause(e).toString(), e);
    }
}
