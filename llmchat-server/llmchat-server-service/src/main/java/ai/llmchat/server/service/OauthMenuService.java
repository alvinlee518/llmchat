package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.TreeNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.vo.RouteRecordVO;
import ai.llmchat.server.repository.entity.OauthMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
public interface OauthMenuService extends IService<OauthMenu> {

    PageData<OauthMenu> queryPage(CommonPageParam param);

    List<TreeNode> treeData(Long parentId);

    List<RouteRecordVO> selectAuthorizedMenuList(Long userId);

    List<String> selectPermissionList(Long userId);
}
