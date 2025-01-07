package ai.llmchat.common.core.sensitive;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

	private final SensitiveType type;

	private final Integer startInclude;

	private final Integer endExclude;

	public SensitiveSerialize() {
		this.type = SensitiveType.NONE;
		this.startInclude = 0;
		this.endExclude = 0;
	}

	public SensitiveSerialize(SensitiveType type, Integer startInclude, Integer endExclude) {
		this.type = type;
		this.startInclude = startInclude;
		this.endExclude = endExclude;
	}

	@Override
	public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		switch (type) {
			// 自定义类型脱敏
			case CUSTOM:
				jsonGenerator.writeString(CharSequenceUtil.hide(str, startInclude, endExclude));
				break;
			// userId脱敏
			case USER_ID:
				jsonGenerator.writeString(String.valueOf(DesensitizedUtil.userId()));
				break;
			// 中文姓名脱敏
			case CHINESE_NAME:
				jsonGenerator.writeString(DesensitizedUtil.chineseName(String.valueOf(str)));
				break;
			// 身份证脱敏
			case ID_CARD:
				jsonGenerator.writeString(DesensitizedUtil.idCardNum(String.valueOf(str), 1, 2));
				break;
			// 固定电话脱敏
			case FIXED_PHONE:
				jsonGenerator.writeString(DesensitizedUtil.fixedPhone(String.valueOf(str)));
				break;
			// 手机号脱敏
			case MOBILE_PHONE:
				jsonGenerator.writeString(DesensitizedUtil.mobilePhone(String.valueOf(str)));
				break;
			// 地址脱敏
			case ADDRESS:
				jsonGenerator.writeString(DesensitizedUtil.address(String.valueOf(str), 8));
				break;
			// 邮箱脱敏
			case EMAIL:
				jsonGenerator.writeString(DesensitizedUtil.email(String.valueOf(str)));
				break;
			// 密码脱敏
			case PASSWORD:
				jsonGenerator.writeString(DesensitizedUtil.password(String.valueOf(str)));
				break;
			// 中国车牌脱敏
			case CAR_LICENSE:
				jsonGenerator.writeString(DesensitizedUtil.carLicense(String.valueOf(str)));
				break;
			// 银行卡脱敏
			case BANK_CARD:
				jsonGenerator.writeString(DesensitizedUtil.bankCard(String.valueOf(str)));
				break;
			default:
				jsonGenerator.writeString(str);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		if (beanProperty != null) {
			// 判断数据类型是否为String类型
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				// 获取定义的注解
				DataSensitive desensitization = beanProperty.getAnnotation(DataSensitive.class);
				// 为null
				if (desensitization == null) {
					desensitization = beanProperty.getContextAnnotation(DataSensitive.class);
				}
				// 不为null
				if (desensitization != null) {
					// 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。
					return new SensitiveSerialize(desensitization.type(), desensitization.startInclude(),
							desensitization.endExclude());
				}
			}

			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}

}
