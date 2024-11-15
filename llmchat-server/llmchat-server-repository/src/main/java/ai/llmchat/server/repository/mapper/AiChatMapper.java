package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.entity.AiChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 对话 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-11-07
 */
@Mapper
public interface AiChatMapper extends BaseMapper<AiChat> {

}