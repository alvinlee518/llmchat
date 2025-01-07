package ai.llmchat.common.core.wrapper.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page = 1;

	private int size = 10;

	public int getPage() {
		if (page <= 0) {
			page = 1;
		}
		return page;
	}

	public int getSize() {
		if (size <= 0) {
			size = 10;
		}
		return size;
	}

	public int getOffset() {
		return (getPage() - 1) * getSize();
	}

}
