package ai.llmchat.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

@Slf4j
public class I18nUtils implements ApplicationContextAware {

	private static MessageSource messageSource;

	public static String getMessage(String messageCode) {
		return getMessage(messageCode, null);
	}

	public static String getMessage(String messageCode, Object[] args) {
		try {
			return messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale());
		}
		catch (NoSuchMessageException e) {
			log.error("no message.", e);
		}
		return messageCode;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		I18nUtils.messageSource = applicationContext.getBean(MessageSource.class);
	}

}
