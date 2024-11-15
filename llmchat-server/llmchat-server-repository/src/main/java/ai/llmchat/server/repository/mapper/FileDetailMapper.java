package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.entity.FileDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件记录表 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Mapper
public interface FileDetailMapper extends BaseMapper<FileDetail> {

}
