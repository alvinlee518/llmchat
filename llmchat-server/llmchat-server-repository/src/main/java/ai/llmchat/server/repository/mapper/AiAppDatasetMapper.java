package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.dataobject.AppDatasetDO;
import ai.llmchat.server.repository.dataobject.DatasetItemDO;
import ai.llmchat.server.repository.entity.AiAppDataset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应用数据集关联表 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
@Mapper
public interface AiAppDatasetMapper extends BaseMapper<AiAppDataset> {
    List<AppDatasetDO> queryPage(@Param("appId") Long appId, @Param("keyword") String keyword);

    List<DatasetItemDO> listByAppId(@Param("appId") Long appId);
}
