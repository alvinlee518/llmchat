package ai.llmchat.common.langchain.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum CleanPatternEnum implements BaseEnum<Integer> {

	REMOVE_EXTRA_SPACES(1, "替换掉连续的空格、换行符和制表符"), REMOVE_URLS_EMAILS(2, "移除URL和电子邮件地址"),
	REMOVE_HTML_TAGS(3, "移除Html格式字符并解析出Html文本");

	private final Integer code;

	private final String message;

	CleanPatternEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

}
