package ai.llmchat.common.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class RequestUtils {
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    public static String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return getToken(request);
        }
        return StringUtils.EMPTY;
    }

    public static String getToken(HttpServletRequest request) {
        String tokenValue = extractTokenValue(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (StringUtils.isBlank(tokenValue)) {
            tokenValue = request.getParameter("access_token");
        }
        return StringUtils.trimToEmpty(tokenValue);
    }

    private static String extractTokenValue(String tokenValue) {
        if (StringUtils.isNotBlank(tokenValue)) {
            if (StringUtils.startsWithIgnoreCase(tokenValue, BEARER_TOKEN_TYPE)) {
                tokenValue = tokenValue.substring(BEARER_TOKEN_TYPE.length()).trim();
            }
            int commaIndex = tokenValue.indexOf(',');
            if (commaIndex > 0) {
                tokenValue = tokenValue.substring(0, commaIndex);
            }
            return tokenValue;
        }
        return null;
    }
}
