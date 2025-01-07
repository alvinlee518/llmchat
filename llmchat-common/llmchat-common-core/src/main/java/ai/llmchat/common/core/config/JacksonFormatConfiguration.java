package ai.llmchat.common.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonFormatConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

	private static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final DateTimeFormatter NORM_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN);

	public static final DateTimeFormatter NORM_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final DateTimeFormatter NORM_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public void customize(Jackson2ObjectMapperBuilder builder) {
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		// yyyy-MM-dd HH:mm:ss
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(NORM_DATETIME_FORMATTER));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(NORM_DATETIME_FORMATTER));
		// yyyy-MM-dd
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(NORM_DATE_FORMATTER));
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(NORM_DATE_FORMATTER));
		// HH:mm:ss
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(NORM_TIME_FORMATTER));
		javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(NORM_TIME_FORMATTER));
		// Instant 类型序列化
		javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
		javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);

		builder.modules(javaTimeModule, new ParameterNamesModule(), new Jdk8Module())
			.failOnUnknownProperties(false)
			.failOnEmptyBeans(false)
			.indentOutput(false)
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.locale(Locale.getDefault())
			.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
			.simpleDateFormat(NORM_DATETIME_PATTERN)
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
					DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
			.serializerByType(Long.class, ToStringSerializer.instance);
	}

}
