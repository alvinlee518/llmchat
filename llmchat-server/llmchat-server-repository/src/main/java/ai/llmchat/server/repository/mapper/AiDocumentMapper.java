package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.dataobject.DocumentItemDO;
import ai.llmchat.server.repository.entity.AiDocument;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据文档 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Mapper
public interface AiDocumentMapper extends BaseMapper<AiDocument> {
    List<DocumentItemDO> queryPage(@Param("datasetId") Long datasetId, @Param("name") String name, @Param("state") Integer state);
}
