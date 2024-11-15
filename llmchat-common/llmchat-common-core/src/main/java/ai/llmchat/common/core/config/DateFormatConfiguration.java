package ai.llmchat.common.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DateFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(JacksonFormatConfiguration.NORM_TIME_FORMATTER);
        registrar.setDateFormatter(JacksonFormatConfiguration.NORM_DATE_FORMATTER);
        registrar.setDateTimeFormatter(JacksonFormatConfiguration.NORM_DATETIME_FORMATTER);
        registrar.registerFormatters(registry);
    }
}
