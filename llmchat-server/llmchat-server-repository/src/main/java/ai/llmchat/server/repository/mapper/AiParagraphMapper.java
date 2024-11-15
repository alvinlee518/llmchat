package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.dataobject.ParagraphDO;
import ai.llmchat.server.repository.entity.AiParagraph;
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
public interface AiParagraphMapper extends BaseMapper<AiParagraph> {
    List<ParagraphDO> querySegmentByIds(@Param("ids") List<Long> ids);
}
