package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.entity.AiApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 应用 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
@Mapper
public interface AiAppMapper extends BaseMapper<AiApp> {

}
