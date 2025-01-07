package ai.llmchat.common.langchain.rag.output;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PPTOutline {

	private String title;

	private String subTitle;

	private List<ChapterItem> chapters;

	@Getter
	@Setter
	public static class ChapterItem {

		private String chapterTitle;

		private List<String> chapterContents;

	}

}
