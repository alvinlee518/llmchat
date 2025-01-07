package ai.llmchat.common.core.util;

import java.util.ArrayList;
import java.util.List;

public class KeyFormatUtils {

	private List<String> list = new ArrayList<>(2);

	private KeyFormatUtils(String prefix) {
		this.list.add(prefix);
	}

	public static KeyFormatUtils of(String prefix) {
		return new KeyFormatUtils(prefix);
	}

	public KeyFormatUtils add(String value) {
		list.add(value);
		return this;
	}

	public String format() {
		return this.toString();
	}

	@Override
	public String toString() {
		return String.join(":", list);
	}

}
