package ai.llmchat.common.langchain.rag;

import ai.llmchat.common.langchain.rag.output.PPTOutline;
import ai.llmchat.common.langchain.rag.output.PPTSlide;
import ai.llmchat.common.langchain.rag.output.Prompt;
import ai.llmchat.common.langchain.rag.output.Suggestion;
import dev.langchain4j.service.*;

public interface AIAssistant {

	/**
	 * 会话
	 * @param memoryId
	 * @param message
	 * @return
	 */
	TokenStream streamingChat(@MemoryId Object memoryId, @UserMessage String message);

	/**
	 * 优化提示词
	 * @param instruction
	 * @return
	 */
	@UserMessage(PromptConstants.PROMPT_GENERATE_TEMPLATE)
	String optimizePrompt(@V("TASK_DESCRIPTION") String instruction);

	/**
	 * 解析提示词
	 * @param instruction
	 * @param prompt
	 * @return
	 */
	@SystemMessage(PromptConstants.PROLOGUE_GENERATE_TEMPLATE)
	@UserMessage(PromptConstants.EXTRACT_PROMPT_TEMPLATE)
	Prompt extractPrompt(@V("TASK_DESCRIPTION") String instruction, @V("INPUT_TEXT") String prompt);

	/**
	 * 问题建议
	 * @param histories
	 * @return
	 */
	@UserMessage(PromptConstants.SUGGESTED_PROMPT_TEMPLATE)
	Suggestion suggestedQuestions(@V("CHAT_HISTORIES") String histories);

	/**
	 * 根据主题生成PPT大纲
	 * @param topic
	 * @return
	 */
	@UserMessage(PromptConstants.PPT_OUTLINE_PROMPT)
	PPTOutline pptOutline(@V("topic") String topic);

	/**
	 * 生成幻灯片详情
	 * @param topic
	 * @return
	 */
	@UserMessage(PromptConstants.PPT_SLIDE_PROMPT)
	PPTSlide pptSlide(@V("topic") String topic, @V("outline") String outline, @V("chapter") String chapter);

	@SystemMessage(PromptConstants.CHINESE_ENGLISH_TRANSLATOR_PROMPT)
	String translate(@V("language") String language, @UserMessage String message);

}
