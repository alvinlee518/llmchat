package ai.llmchat.common.mybatis.util;

import ai.llmchat.common.mybatis.annotation.DataScope;
import ai.llmchat.common.mybatis.exception.ExceptionFactory;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DataScopeUtils {

	private static final Map<String, DataScope> DATA_SCOPE_MAP = new ConcurrentHashMap<>();

	/**
	 * 获取data scope数据权限注解
	 * @param statementId
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static DataScope getDataScope(String statementId) {
		DataScope dataScope = DATA_SCOPE_MAP.get(statementId);
		if (Objects.isNull(dataScope)) {
			String className = getClassName(statementId);
			if (StringUtils.isBlank(className)) {
				if (log.isDebugEnabled()) {
					log.debug("current statement {} can not found class", statementId);
				}
				return null;
			}
			try {
				Class<?> mapperClass = Class.forName(className);
				Method[] mapperMethods = mapperClass.getMethods();
				for (Method method : mapperMethods) {
					DataScope methodAnnotation = method.getAnnotation(DataScope.class);
					if (Objects.nonNull(methodAnnotation)) {
						String cacheKey = className + "." + method.getName();
						if (cacheKey.equals(statementId)) {
							dataScope = methodAnnotation;
						}
						DATA_SCOPE_MAP.put(cacheKey, methodAnnotation);
					}
				}
				DataScope classAnnotation = mapperClass.getAnnotation(DataScope.class);
				DATA_SCOPE_MAP.put(className, classAnnotation);
				if (Objects.isNull(dataScope)) {
					dataScope = classAnnotation;
				}
			}
			catch (Exception ex) {
				throw ExceptionFactory.dataScope(
						String.format("failed to get data scope annotation,error statement id %s", statementId), ex);
			}
		}
		return dataScope;
	}

	/**
	 * 获取mapper class columnName
	 * @param statementId
	 * @return
	 */
	private static String getClassName(String statementId) {
		int lastIndex = statementId.lastIndexOf(".");
		return lastIndex > 0 ? statementId.substring(0, lastIndex) : null;
	}

}
