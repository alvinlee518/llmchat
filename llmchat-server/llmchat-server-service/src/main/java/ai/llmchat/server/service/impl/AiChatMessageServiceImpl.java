package ai.llmchat.server.service.impl;

import ai.llmchat.server.repository.entity.AiChatMessage;
import ai.llmchat.server.repository.mapper.AiChatMessageMapper;
import ai.llmchat.server.service.AiChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 对话 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-11-07
 */
@Service
public class AiChatMessageServiceImpl extends ServiceImpl<AiChatMessageMapper, AiChatMessage>
		implements AiChatMessageService {

}
