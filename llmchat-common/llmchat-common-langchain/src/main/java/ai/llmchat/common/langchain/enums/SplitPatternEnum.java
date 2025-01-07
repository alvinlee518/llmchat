package ai.llmchat.common.langchain.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum SplitPatternEnum implements BaseEnum<Integer> {

	MD_H1(1, "#", "(?<=^)# .*|(?<=\\\\n)# .*"), MD_H2(2, "##", "(?<=\\\\n)(?<!#)## (?!#).*|(?<=^)(?<!#)## (?!#).*"),
	MD_H3(3, "###", "(?<=\\\\n)(?<!#)### (?!#).*|(?<=^)(?<!#)### (?!#).*"),
	MD_H4(4, "####", "(?<=\\\\n)(?<!#)#### (?!#).*|(?<=^)(?<!#)#### (?!#).*"),
	MD_H5(5, "#####", "(?<=\\\\n)(?<!#)##### (?!#).*|(?<=^)(?<!#)##### (?!#).*"),
	MD_H6(6, "######", "(?<=\\\\n)(?<!#)###### (?!#).*|(?<=^)(?<!#)###### (?!#).*"), HYPHEN(7, "-", "(?<! )- .*"),
	SPACE(8, "空格", "(?<! ) (?! )"), SEMICOLON(9, "分号", "(?<!；)；(?!；)"), COMMA(10, "逗号", "(?<!，)，(?!，)"),
	PERIOD(11, "句号", "(?<!。)。(?!。)"), ENTER(12, "回车", "(?<!\\\\n)\\\\n(?!\\\\n)"),
	BLANK_LINE(13, "空行", "(?<!\\\\n)\\\\n\\\\n(?!\\\\n)");

	private final Integer code;

	private final String message;

	private final String pattern;

	SplitPatternEnum(Integer code, String message, String pattern) {
		this.code = code;
		this.message = message;
		this.pattern = pattern;
	}

}
