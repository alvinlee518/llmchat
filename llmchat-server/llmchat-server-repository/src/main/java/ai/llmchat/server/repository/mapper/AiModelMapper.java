package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.dataobject.ModelOptionsDO;
import ai.llmchat.server.repository.entity.AiModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 模型配置 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Mapper
public interface AiModelMapper extends BaseMapper<AiModel> {

	ModelOptionsDO getModelByDatasetId(@Param("datasetId") Long datasetId);

	ModelOptionsDO getModelByAppId(@Param("appId") Long appId);

	ModelOptionsDO getModelByChatId(@Param("chatId") Long chatId);

}
