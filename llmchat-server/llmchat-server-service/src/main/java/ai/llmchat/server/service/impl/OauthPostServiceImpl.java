package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.exception.DataExistsException;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthPost;
import ai.llmchat.server.repository.mapper.OauthPostMapper;
import ai.llmchat.server.service.OauthPostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 岗位 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@Service
public class OauthPostServiceImpl extends ServiceImpl<OauthPostMapper, OauthPost> implements OauthPostService {

	@Override
	public PageData<OauthPost> queryPage(CommonPageParam param) {
		LambdaQueryWrapper<OauthPost> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, OauthPost::getStatus, param.getStatus())
			.like(StringUtils.isNotBlank(param.getName()), OauthPost::getName, param.getName())
			.orderByDesc(OauthPost::getUpdateAt);
		PageInfo<OauthPost> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> this.list(queryWrapper));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
	}

	@Override
	public List<SelectNode> selectOptions() {
		LambdaQueryWrapper<OauthPost> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(OauthPost::getStatus, BooleanEnum.YES.getCode())
			.select(OauthPost::getName, OauthPost::getId)
			.orderByDesc(OauthPost::getSorting);
		return this.list(queryWrapper).stream().map(item -> new SelectNode(item.getName(), item.getId())).toList();
	}

	@Override
	public boolean saveOrUpdate(OauthPost param) {
		LambdaQueryWrapper<OauthPost> queryWrapper = Wrappers.<OauthPost>lambdaQuery()
			.eq(OauthPost::getCode, param.getCode())
			.ne(OauthPost::getId, Optional.ofNullable(param.getId()).orElse(0L));
		if (exists(queryWrapper)) {
			throw new DataExistsException("岗位编码已存在,请修改后重试!");
		}
		return super.saveOrUpdate(param);
	}

}
