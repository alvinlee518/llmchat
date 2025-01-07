package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.exception.DataExistsException;
import ai.llmchat.common.core.util.TreeNodeUtils;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.TreeNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthDept;
import ai.llmchat.server.repository.mapper.OauthDeptMapper;
import ai.llmchat.server.service.OauthDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@Service
public class OauthDeptServiceImpl extends ServiceImpl<OauthDeptMapper, OauthDept> implements OauthDeptService {

	@Override
	public PageData<OauthDept> queryPage(CommonPageParam param) {
		LambdaQueryWrapper<OauthDept> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, OauthDept::getStatus, param.getStatus())
			.eq(Optional.ofNullable(param.getParentId()).orElse(-1L) >= 0, OauthDept::getParentId, param.getParentId())
			.like(StringUtils.isNotBlank(param.getName()), OauthDept::getName, param.getName())
			.orderByDesc(OauthDept::getUpdateAt);
		PageInfo<OauthDept> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> this.list(queryWrapper));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
	}

	@Override
	public List<TreeNode> treeData(Long parentId) {
		LambdaQueryWrapper<OauthDept> queryWrapper = new LambdaQueryWrapper<OauthDept>()
			.eq(OauthDept::getStatus, BooleanEnum.YES.getCode())
			.ge(Objects.nonNull(parentId), OauthDept::getParentId, parentId)
			.orderByDesc(OauthDept::getSorting);
		List<TreeNode> list = this.list(queryWrapper)
			.stream()
			.map(item -> new TreeNode(item.getName(), item.getId(), item.getParentId()))
			.toList();
		return TreeNodeUtils.toTree(list);
	}

	@Override
	public boolean saveOrUpdate(OauthDept param) {
		LambdaQueryWrapper<OauthDept> queryWrapper = Wrappers.<OauthDept>lambdaQuery()
			.eq(OauthDept::getCode, param.getCode())
			.ne(OauthDept::getId, Optional.ofNullable(param.getId()).orElse(0L));
		if (exists(queryWrapper)) {
			throw new DataExistsException("部门编码已存在,请修改后重试!");
		}
		return super.saveOrUpdate(param);
	}

}
