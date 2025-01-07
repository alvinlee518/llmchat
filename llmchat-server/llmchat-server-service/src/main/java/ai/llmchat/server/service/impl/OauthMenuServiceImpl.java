package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.util.TreeNodeUtils;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.TreeNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.vo.RouteMetaVO;
import ai.llmchat.server.api.vo.RouteRecordVO;
import ai.llmchat.server.repository.entity.OauthMenu;
import ai.llmchat.server.repository.mapper.OauthMenuMapper;
import ai.llmchat.server.service.OauthMenuService;
import cn.hutool.core.text.NamingCase;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@Service
public class OauthMenuServiceImpl extends ServiceImpl<OauthMenuMapper, OauthMenu> implements OauthMenuService {

	@Override
	public PageData<OauthMenu> queryPage(CommonPageParam param) {
		LambdaQueryWrapper<OauthMenu> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, OauthMenu::getStatus,
				param.getStatus());
		queryWrapper.eq(Optional.ofNullable(param.getParentId()).orElse(-1L) >= 0, OauthMenu::getParentId,
				param.getParentId());
		queryWrapper.like(StringUtils.isNotBlank(param.getName()), OauthMenu::getName, param.getName());
		queryWrapper.orderByDesc(OauthMenu::getUpdateAt);
		PageInfo<OauthMenu> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> this.list(queryWrapper));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
	}

	@Override
	public List<TreeNode> treeData(Long parentId) {
		LambdaQueryWrapper<OauthMenu> queryWrapper = new LambdaQueryWrapper<OauthMenu>()
			.eq(OauthMenu::getStatus, BooleanEnum.YES.getCode())
			.ge(Objects.nonNull(parentId), OauthMenu::getParentId, parentId)
			.orderByDesc(OauthMenu::getSorting);
		List<TreeNode> list = this.list(queryWrapper)
			.stream()
			.map(item -> new TreeNode(item.getName(), item.getId(), item.getParentId()))
			.toList();
		return TreeNodeUtils.toTree(list);
	}

	@Override
	public List<RouteRecordVO> selectAuthorizedMenuList(Long userId) {
		List<OauthMenu> oauthMenus = baseMapper.selectAuthorizedMenuList(userId);
		return buildRouteTree(0L, oauthMenus);
	}

	@Override
	public List<String> selectPermissionList(Long userId) {
		return baseMapper.selectPermissionList(userId);
	}

	private List<RouteRecordVO> buildRouteTree(Long parentId, List<OauthMenu> list) {
		if (CollectionUtils.isEmpty(list)) {
			return List.of();
		}
		List<RouteRecordVO> result = new ArrayList<>();
		for (OauthMenu item : list) {
			if (Objects.equals(parentId, item.getParentId())) {
				List<RouteRecordVO> children = buildRouteTree(item.getId(), list);
				List<String> perms = null;
				if (StringUtils.isNotBlank(item.getPerms())) {
					perms = new ArrayList<>();
					perms.add(item.getPerms());
				}
				RouteMetaVO meta = new RouteMetaVO(item.getName(), perms, Objects.equals(0, item.getKeepAlive()),
						Objects.equals(1, item.getHidden()), Objects.equals(1, item.getTarget()));
				String name = NamingCase.toPascalCase(StringUtils.replace(item.getPath(), "/", "_"));
				RouteRecordVO route = new RouteRecordVO(name, item.getPath(), item.getComponent(), meta, children);
				result.add(route);
			}
		}
		return result;
	}

}
