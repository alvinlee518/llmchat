package ai.llmchat.common.security.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		boolean isFirstRequest = !isAsyncDispatch(request);
		HttpServletRequest requestToUse = request;
		if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
			requestToUse = new ContentCachingRequestWrapper(request);
		}
		HttpServletResponse responseToUse = response;
		if (isFirstRequest && !(response instanceof ContentCachingResponseWrapper)
				&& StringUtils.equalsIgnoreCase("application/json", response.getContentType())) {
			responseToUse = new ContentCachingResponseWrapper(response);
		}
		long start = System.currentTimeMillis();
		try {
			filterChain.doFilter(requestToUse, responseToUse);
		}
		finally {
			if (isFirstRequest) {
				String header = getRequestHeader(request);
				String payload = getRequestBody(requestToUse);
				String result = getResponseBody(responseToUse);
				int responseStatus = responseToUse.getStatus();
				if (responseStatus != HttpStatus.OK.value()) {
					log.error(
							"\nTime Consuming : {} ms \nRequest URI: {} {}?{} \nRequest Header : {} \nRequest Body : {} \nResponse Status : {}\nResponse Body : {}",
							(System.currentTimeMillis() - start), request.getMethod(), request.getRequestURI(),
							request.getQueryString(), header, payload, responseStatus, result);
				}
				else {
					log.info(
							"\nTime Consuming : {} ms \nRequest URI: {} {}?{} \nRequest Header : {} \nRequest Body : {} \nResponse Status : {}\nResponse Body : {}",
							(System.currentTimeMillis() - start), request.getMethod(), request.getRequestURI(),
							request.getQueryString(), header, payload, responseStatus, result);
				}
			}
		}

	}

	/**
	 * 获取请求体
	 * @param request
	 * @return
	 */
	private String getRequestBody(HttpServletRequest request) {
		String payload = "";
		try {
			// 文件上传
			if (StringUtils.startsWithIgnoreCase(request.getContentType(), "multipart/")) {
				return payload;
			}
			ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
					ContentCachingRequestWrapper.class);
			if (Objects.nonNull(wrapper)) {
				byte[] contentAsByteArray = wrapper.getContentAsByteArray();
				if (contentAsByteArray.length > 0) {
					payload = new String(contentAsByteArray, 0, contentAsByteArray.length);
				}
			}
		}
		catch (Exception e) {
			log.error("获取请求体异常", e);
			return "[unknown]";
		}
		return payload;
	}

	/**
	 * 获取响应体
	 * @param response
	 * @return
	 */
	private String getResponseBody(HttpServletResponse response) {
		String payload = "";
		try {
			ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
					ContentCachingResponseWrapper.class);
			if (Objects.nonNull(wrapper)) {
				byte[] contentAsByteArray = wrapper.getContentAsByteArray();
				if (contentAsByteArray.length > 0) {
					payload = new String(contentAsByteArray, 0, contentAsByteArray.length);
				}
				// 重要
				wrapper.copyBodyToResponse();
			}
		}
		catch (Exception e) {
			log.error("获取响应体异常", e);
			return "[unknown]";
		}
		return payload;
	}

	/**
	 * 获取请求头
	 * @param request
	 * @return
	 */
	private String getRequestHeader(HttpServletRequest request) {
		StringBuilder result = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			value = URLDecoder.decode(value, StandardCharsets.UTF_8);
			result.append(String.format("\n%s=%s", name, value));
		}
		return result.toString();
	}

}
