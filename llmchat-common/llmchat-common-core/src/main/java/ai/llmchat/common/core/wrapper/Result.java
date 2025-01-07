package ai.llmchat.common.core.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;

	private String message;

	private Long timestamp;

	private T data;

	public Result() {
		this.timestamp = System.currentTimeMillis();
	}

	public Result(Integer code, String message) {
		this();
		this.code = code;
		this.message = message;
	}

	public Result(Integer code, String message, T data) {
		this(code, message);
		this.data = data;
	}

	public static <T> Result<T> success() {
		return new Result<>(200, "success");
	}

	public static <T> Result<T> success(String msg) {
		return new Result<>(200, msg);
	}

	public static <T> Result<T> data(T data) {
		return new Result<T>(200, "success", data);
	}

	public static <T> Result<T> data(T data, String message) {
		return new Result<T>(200, message, data);
	}

	public static <T> Result<T> fail() {
		return new Result<>(500, "failed");
	}

	public static <T> Result<T> fail(String message) {
		return new Result<T>(500, message);
	}

	public static <T> Result<T> fail(int code, String message) {
		return new Result<T>(code, message);
	}

	public static <T> Result<T> badRequest(String message) {
		return new Result<T>(400, message);
	}

	public static <T> Result<T> unauthorized(String message) {
		return new Result<>(401, message);
	}

	public static <T> Result<T> forbidden(String message) {
		return new Result<>(403, message);
	}

	public static <T> Result<T> condition(boolean flag) {
		return flag ? success() : fail();
	}

}
