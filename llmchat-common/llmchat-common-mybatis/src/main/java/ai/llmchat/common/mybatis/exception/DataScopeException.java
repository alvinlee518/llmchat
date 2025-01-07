package ai.llmchat.common.mybatis.exception;

/**
 * 数据权限异常
 *
 * @author lxw
 */
public class DataScopeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataScopeException(String message) {
		super(message);
	}

	public DataScopeException(Throwable throwable) {
		super(throwable);
	}

	public DataScopeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
