package ai.llmchat.common.core.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface DataSensitive {

	/**
	 * 脱敏数据类型
	 * @return
	 */
	SensitiveType type() default SensitiveType.NONE;

	/**
	 * 脱敏开始位置（包含）
	 */
	int startInclude() default 0;

	/**
	 * 脱敏结束位置（不包含）
	 */
	int endExclude() default 0;

}
