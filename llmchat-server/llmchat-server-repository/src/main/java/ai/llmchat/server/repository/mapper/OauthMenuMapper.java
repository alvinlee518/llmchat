package ai.llmchat.server.repository.mapper;

import ai.llmchat.server.repository.entity.OauthMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Mapper
public interface OauthMenuMapper extends BaseMapper<OauthMenu> {

	List<OauthMenu> selectAuthorizedMenuList(@Param("userId") Long userId);

	List<String> selectPermissionList(@Param("userId") Long userId);

}
