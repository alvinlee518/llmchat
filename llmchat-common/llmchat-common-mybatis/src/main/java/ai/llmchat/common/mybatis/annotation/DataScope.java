package ai.llmchat.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据权限数据权限注解
 *
 * @author lxw
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface DataScope {

	/**
	 * 数据权限关联表别名
	 * @return
	 */
	String alias() default "";

	/**
	 * 数据权限关联表字段
	 * @return
	 */
	String column() default "";

	/**
	 * 是否禁用数据权限过滤
	 * @return
	 */
	boolean ignore() default false;

}
