package ai.llmchat.common.core.util;

import org.springframework.util.StringUtils;

import java.util.Set;

public class StringFormats {

	/**
	 * 下划线字符
	 */
	private static final char UNDERLINE = '_';

	/**
	 * 下划线转驼峰
	 * @param param
	 * @return
	 */
	public static String underlineToCamel(String param) {
		if (!StringUtils.hasText(param)) {
			return "";
		}
		String temp = param.toLowerCase();
		int len = temp.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = temp.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(temp.charAt(i)));
				}
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 * @param param
	 * @return
	 */
	public static String camelToUnderline(String param) {
		if (!StringUtils.hasText(param)) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append(UNDERLINE);
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	/**
	 * 首字母大写
	 * @param param
	 * @return
	 */
	public static String firstToUpperCase(String param) {
		if (StringUtils.hasText(param)) {
			return param.substring(0, 1).toUpperCase() + param.substring(1);
		}
		return "";
	}

	/**
	 * 首字母小写
	 * @param param
	 * @return
	 */
	public static String firstToLowerCase(String param) {
		if (!StringUtils.hasText(param)) {
			return "";
		}
		return param.substring(0, 1).toLowerCase() + param.substring(1);
	}

	/**
	 * 去掉指定前缀
	 * @param param
	 * @param prefix
	 * @return
	 */
	public static String removePrefix(String param, Set<String> prefix) {
		if (!StringUtils.hasText(param)) {
			return "";
		}
		// 判断是否有匹配的前缀，然后截取前缀
		return prefix.stream()
			.filter(pf -> param.toLowerCase().startsWith(pf.toLowerCase()))
			.findFirst()
			.map(pf -> param.substring(pf.length()))
			.orElse(param);
	}

	/**
	 * 去掉指定后缀
	 * @param param
	 * @param suffix
	 * @return
	 */
	public static String removeSuffix(String param, Set<String> suffix) {
		if (!StringUtils.hasText(param)) {
			return "";
		}
		// 判断是否有匹配的后缀，然后截取后缀
		return suffix.stream()
			.filter(sf -> param.toLowerCase().endsWith(sf.toLowerCase()))
			.findFirst()
			.map(sf -> param.substring(0, param.length() - sf.length()))
			.orElse(param);
	}

}
