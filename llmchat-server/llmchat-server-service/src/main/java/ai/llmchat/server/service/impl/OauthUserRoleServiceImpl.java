package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.UserRolePageParam;
import ai.llmchat.server.api.param.UserRoleParam;
import ai.llmchat.server.repository.entity.OauthUser;
import ai.llmchat.server.repository.entity.OauthUserRole;
import ai.llmchat.server.repository.mapper.OauthUserMapper;
import ai.llmchat.server.repository.mapper.OauthUserRoleMapper;
import ai.llmchat.server.service.OauthUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Service
public class OauthUserRoleServiceImpl extends ServiceImpl<OauthUserRoleMapper, OauthUserRole>
		implements OauthUserRoleService {

	private final OauthUserMapper oauthUserMapper;

	public OauthUserRoleServiceImpl(OauthUserMapper oauthUserMapper) {
		this.oauthUserMapper = oauthUserMapper;
	}

	@Override
	public void batchSave(Long userId, List<Long> ids) {
		remove(Wrappers.<OauthUserRole>lambdaQuery().eq(OauthUserRole::getUserId, userId));
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		List<OauthUserRole> list = ids.stream().map(item -> {
			OauthUserRole result = new OauthUserRole();
			result.setRoleId(item);
			result.setUserId(userId);
			return result;
		}).toList();
		saveBatch(list);
	}

	@Override
	public PageData<OauthUser> userRoleList(UserRolePageParam param) {
		PageInfo<OauthUser> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> oauthUserMapper.selectUserListByRole(param.getRoleId(), param.getName(),
					param.getPhone(), param.getUserScope()));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
	}

	@Override
	public void allocated(UserRoleParam param) {
		List<OauthUserRole> list = param.getUserIds().stream().map(item -> {
			OauthUserRole result = new OauthUserRole();
			result.setRoleId(param.getRoleId());
			result.setUserId(item);
			return result;
		}).toList();
		saveBatch(list);
	}

	@Override
	public void unallocated(UserRoleParam param) {
		LambdaQueryWrapper<OauthUserRole> queryWrapper = Wrappers.<OauthUserRole>lambdaQuery()
			.eq(OauthUserRole::getRoleId, param.getRoleId())
			.in(OauthUserRole::getUserId, param.getUserIds());
		remove(queryWrapper);
	}

}
