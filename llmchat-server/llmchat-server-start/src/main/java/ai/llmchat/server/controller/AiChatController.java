package ai.llmchat.server.controller;

import ai.llmchat.common.core.wrapper.Result;
import ai.llmchat.common.langchain.rag.output.Message;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.api.vo.MessageVO;
import ai.llmchat.server.api.vo.PromptVO;
import ai.llmchat.server.service.AiChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * <p>
 * 对话 前端控制器
 * </p>
 *
 * @author lixw
 * @since 2024-11-07
 */
@RestController
@RequestMapping("/chat")
public class AiChatController {
    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping(value = "/debug", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> debugChat(@RequestBody ChatTestingParam param) {
        return aiChatService.debugChat(param);
    }

    @PostMapping(value = "/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamingChat(@RequestBody ChatStramingParam param) {
        return aiChatService.streamingChat(param);
    }

    @PostMapping("/prompt_optimize")
    public Result<PromptVO> optimize(@RequestBody OptimizeParam param) {
        PromptVO generate = aiChatService.optimize(param);
        return Result.data(generate);
    }

    @GetMapping("/suggested/{chatId}")
    public Result<List<String>> suggested(@PathVariable("chatId") Long chatId) {
        List<String> rtn = aiChatService.suggested(chatId);
        return Result.data(rtn);
    }

    @GetMapping("/messages/{chatId}")
    public Result<List<MessageVO>> messages(@PathVariable("chatId") Long chatId) {
        List<MessageVO> messages = aiChatService.chatMessageById(chatId);
        return Result.data(messages);
    }

    @GetMapping("/histories/{appId}")
    public Result<AppChatVO> histories(@PathVariable("appId") Long appId) {
        AppChatVO result = aiChatService.chatListByAppId(appId);
        return Result.data(result);
    }

    @PostMapping("/open")
    public Result<Long> openChat(@RequestBody ChatParam param) {
        Long l = aiChatService.saveOrUpdate(param);
        return Result.data(l);
    }

    @PostMapping("/modify")
    public Result<Long> modify(@RequestBody ChatParam param) {
        Long l = aiChatService.saveOrUpdate(param);
        return Result.data(l);
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") List<Long> ids) {
        aiChatService.removeByIds(ids);
        return Result.success();
    }

    @PostMapping("/rating")
    public Result<?> rating(@RequestBody RatingParam param) {
        aiChatService.rating(param);
        return Result.success();
    }
}
