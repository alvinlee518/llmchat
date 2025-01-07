package ai.llmchat.common.langchain.rag.output;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PPTSlide {

	private String title;

	private String imageKeywords;

	private List<SlideContent> content;

	@Getter
	@Setter
	public static class SlideContent {

		private String mainPoint;

		private List<String> subPoints;

	}

}
