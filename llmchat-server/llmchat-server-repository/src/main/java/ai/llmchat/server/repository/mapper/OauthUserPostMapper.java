package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.entity.OauthUserPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户岗位关联表 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Mapper
public interface OauthUserPostMapper extends BaseMapper<OauthUserPost> {

}
