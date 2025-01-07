package ai.llmchat.common.langchain.enums;

import ai.llmchat.common.core.enums.BaseEnum;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ModelProviderEnum implements BaseEnum<Integer> {

	AZURE_OPEN_AI(1, "Azure OpenAI", "azure_openai.svg"), CHAT_GLM(2, "ChatGLM", "chat_glm.svg"),
	HUGGING_FACE(3, "Hugging Face", "huggingface.svg"), LOCAL_AI(4, "LocalAI", "localai.svg"),
	OLLAMA(5, "Ollama", "ollama.svg"), OPEN_AI(6, "OpenAI", "openai.svg"), QIAN_FAN(7, "千帆大模型", "qianfan.png"),
	QWEN(8, "通义千问", "qwen.png"), ZHIPU_AI(9, "智谱AI", "zhipuai.svg"), XINFERENCE(10, "Xinference", "xinference.svg"),;

	private final Integer code;

	private final String message;

	private final String icon;

	ModelProviderEnum(Integer code, String message, String icon) {
		this.code = code;
		this.message = message;
		this.icon = icon;
	}

	public static ModelProviderEnum valueOf(Integer value) {
		for (ModelProviderEnum item : values()) {
			if (Objects.equals(value, item.getCode())) {
				return item;
			}
		}
		throw new IllegalArgumentException("No enum constant with code " + value);
	}

}
