package ai.llmchat.common.langchain.rag;

import ai.llmchat.common.langchain.rag.output.Prompt;
import ai.llmchat.common.langchain.rag.output.Suggestion;
import dev.langchain4j.service.*;

public interface AIAssistant {
    /**
     * 会话
     *
     * @param memoryId
     * @param message
     * @return
     */
    TokenStream streamingChat(@MemoryId Object memoryId, @UserMessage String message);

    /**
     * 优化提示词
     *
     * @param instruction
     * @return
     */
    @UserMessage(PromptConstants.PROMPT_GENERATE_TEMPLATE)
    String optimizePrompt(@V("TASK_DESCRIPTION") String instruction);

    /**
     * 解析提示词
     *
     * @param instruction
     * @param prompt
     * @return
     */
    @SystemMessage(PromptConstants.PROLOGUE_GENERATE_TEMPLATE)
    @UserMessage(PromptConstants.EXTRACT_PROMPT_TEMPLATE)
    Prompt extractPrompt(@V("TASK_DESCRIPTION") String instruction, @V("INPUT_TEXT") String prompt);

    /**
     * 问题建议
     *
     * @param histories
     * @return
     */
    @UserMessage(PromptConstants.SUGGESTED_PROMPT_TEMPLATE)
    Suggestion suggestedQuestions(@V("CHAT_HISTORIES") String histories);
}
