package ai.llmchat.server.service.impl;

import ai.llmchat.server.repository.entity.OauthRoleDept;
import ai.llmchat.server.repository.entity.OauthRoleMenu;
import ai.llmchat.server.repository.mapper.OauthRoleDeptMapper;
import ai.llmchat.server.service.OauthRoleDeptService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 角色部门关联表 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Service
public class OauthRoleDeptServiceImpl extends ServiceImpl<OauthRoleDeptMapper, OauthRoleDept>
		implements OauthRoleDeptService {

	@Override
	public void batchSave(Long roleId, List<Long> ids) {
		remove(Wrappers.<OauthRoleDept>lambdaQuery().eq(OauthRoleDept::getRoleId, roleId));
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		List<OauthRoleDept> list = ids.stream().map(item -> {
			OauthRoleDept result = new OauthRoleDept();
			result.setDeptId(item);
			result.setRoleId(roleId);
			return result;
		}).toList();
		saveBatch(list);
	}

}
