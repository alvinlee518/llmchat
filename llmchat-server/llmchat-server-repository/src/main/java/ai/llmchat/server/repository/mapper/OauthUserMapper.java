package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.entity.OauthUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Mapper
public interface OauthUserMapper extends BaseMapper<OauthUser> {
    List<OauthUser> selectUserListByRole(@Param("roleId") Long roleId, @Param("name") String name, @Param("phone") String phone, @Param("userScope") Integer userScope);
}
