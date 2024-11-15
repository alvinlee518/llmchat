package ai.llmchat.server.service;

import ai.llmchat.common.langchain.rag.output.Message;
import ai.llmchat.server.api.param.*;
import ai.llmchat.server.api.vo.MessageVO;
import ai.llmchat.server.api.vo.PromptVO;
import ai.llmchat.server.repository.entity.AiChat;
import com.baomidou.mybatisplus.extension.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * <p>
 * 对话 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-11-07
 */
public interface AiChatService extends IService<AiChat> {
    Flux<Message> debugChat(ChatTestingParam param);

    Flux<Message> streamingChat(ChatStramingParam param);

    List<MessageVO> chatMessageById(Long chatId);

    AppChatVO chatListByAppId(Long appId);

    PromptVO optimize(OptimizeParam param);

    List<String> suggested(Long chatId);

    Long saveOrUpdate(ChatParam param);

    void rating(RatingParam param);
}
