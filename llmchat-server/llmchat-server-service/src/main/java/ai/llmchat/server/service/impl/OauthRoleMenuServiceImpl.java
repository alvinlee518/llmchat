package ai.llmchat.server.service.impl;

import ai.llmchat.server.repository.entity.OauthRoleMenu;
import ai.llmchat.server.repository.mapper.OauthRoleMenuMapper;
import ai.llmchat.server.service.OauthRoleMenuService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 角色菜单关联表 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Service
public class OauthRoleMenuServiceImpl extends ServiceImpl<OauthRoleMenuMapper, OauthRoleMenu>
		implements OauthRoleMenuService {

	@Override
	public void batchSave(Long roleId, List<Long> ids) {
		remove(Wrappers.<OauthRoleMenu>lambdaQuery().eq(OauthRoleMenu::getRoleId, roleId));
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		List<OauthRoleMenu> list = ids.stream().map(item -> {
			OauthRoleMenu result = new OauthRoleMenu();
			result.setMenuId(item);
			result.setRoleId(roleId);
			return result;
		}).toList();
		saveBatch(list);
	}

}
