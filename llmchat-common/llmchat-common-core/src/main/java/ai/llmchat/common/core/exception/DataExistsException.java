package ai.llmchat.common.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataExistsException(String message) {
		super(message);
	}

	public DataExistsException(Throwable cause) {
		super(cause);
	}

	public DataExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
