package ai.llmchat.common.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BeanMapUtils {

	/**
	 * Bean To Map
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		Map<String, Object> objectMap = new HashMap<>();
		if (Objects.nonNull(bean)) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				objectMap.put(key + "", beanMap.get(key));
			}
		}
		return objectMap;
	}

	/**
	 * Map To Bean
	 * @param objectMap
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> objectMap, Class<T> clazz) {
		T bean = BeanUtils.instantiateClass(clazz);
		BeanMap beanMap = BeanMap.create(bean);
		beanMap.putAll(objectMap);
		return bean;
	}

}
