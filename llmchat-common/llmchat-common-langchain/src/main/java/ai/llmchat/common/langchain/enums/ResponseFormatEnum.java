package ai.llmchat.common.langchain.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum ResponseFormatEnum implements BaseEnum<Integer> {

	TEXT(0, "text"), JSON_OBJECT(1, "json_object");

	private final Integer code;

	private final String message;

	ResponseFormatEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

}
