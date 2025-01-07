package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.dataobject.DatasetDO;
import ai.llmchat.server.repository.entity.AiDataset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据集 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Mapper
public interface AiDatasetMapper extends BaseMapper<AiDataset> {

	List<DatasetDO> queryPage(@Param("name") String name);

	List<AiDataset> listByAppId(@Param("appId") Long appId);

}
