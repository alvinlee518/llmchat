package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.exception.AuthenticationException;
import ai.llmchat.common.core.exception.DataExistsException;
import ai.llmchat.common.core.exception.ServiceException;
import ai.llmchat.common.core.util.IPUtils;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.security.SecurityClaims;
import ai.llmchat.common.security.SecurityUtils;
import ai.llmchat.common.security.service.PasswordEncryptService;
import ai.llmchat.common.security.service.SecurityClaimsService;
import ai.llmchat.server.api.param.OauthUserParam;
import ai.llmchat.server.api.param.OauthUserPageParam;
import ai.llmchat.server.api.vo.OauthUserVO;
import ai.llmchat.server.repository.entity.OauthDept;
import ai.llmchat.server.repository.entity.OauthUser;
import ai.llmchat.server.repository.entity.OauthUserPost;
import ai.llmchat.server.repository.entity.OauthUserRole;
import ai.llmchat.server.repository.mapper.OauthUserMapper;
import ai.llmchat.server.service.OauthDeptService;
import ai.llmchat.server.service.OauthUserPostService;
import ai.llmchat.server.service.OauthUserRoleService;
import ai.llmchat.server.service.OauthUserService;
import ai.llmchat.server.service.converter.OauthUserConverter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OauthUserServiceImpl extends ServiceImpl<OauthUserMapper, OauthUser>
		implements OauthUserService, SecurityClaimsService {

	private final OauthDeptService oauthDeptService;

	private final OauthUserConverter oauthUserConverter;

	private final OauthUserRoleService oauthUserRoleService;

	private final OauthUserPostService oauthUserPostService;

	private final PasswordEncryptService passwordEncryptService;

	public OauthUserServiceImpl(OauthDeptService oauthDeptService, OauthUserConverter oauthUserConverter,
			OauthUserRoleService oauthUserRoleService, OauthUserPostService oauthUserPostService,
			PasswordEncryptService passwordEncryptService) {
		this.oauthDeptService = oauthDeptService;
		this.oauthUserConverter = oauthUserConverter;
		this.oauthUserRoleService = oauthUserRoleService;
		this.oauthUserPostService = oauthUserPostService;
		this.passwordEncryptService = passwordEncryptService;
	}

	@Override
	public PageData<OauthUserVO> queryPage(OauthUserPageParam param) {
		LambdaQueryWrapper<OauthUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, OauthUser::getStatus,
				param.getStatus());
		queryWrapper.eq(Optional.ofNullable(param.getDeptId()).orElse(0L) >= 1, OauthUser::getDeptId,
				param.getDeptId());
		queryWrapper.like(StringUtils.isNotBlank(param.getName()), OauthUser::getName, param.getName());
		queryWrapper.like(StringUtils.isNotBlank(param.getPhone()), OauthUser::getPhone, param.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(param.getEmail()), OauthUser::getEmail, param.getEmail());
		queryWrapper.orderByDesc(OauthUser::getUpdateAt);
		PageInfo<OauthUser> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> baseMapper.selectList(queryWrapper));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(),
				oauthUserConverter.dto2vo(pageInfo.getList()));
	}

	@Override
	public Long saveOrUpdate(OauthUserParam param) {
		if (exists(Wrappers.<OauthUser>lambdaQuery()
			.eq(OauthUser::getUsername, param.getUsername())
			.ne(OauthUser::getId, Optional.ofNullable(param.getId()).orElse(0L)))) {
			throw new DataExistsException("用户名已存在,请修改后重试!");
		}
		if (exists(Wrappers.<OauthUser>lambdaQuery()
			.eq(OauthUser::getEmail, param.getEmail())
			.ne(OauthUser::getId, Optional.ofNullable(param.getId()).orElse(0L)))) {
			throw new DataExistsException("邮箱已存在,请修改后重试!");
		}
		if (exists(Wrappers.<OauthUser>lambdaQuery()
			.eq(OauthUser::getPhone, param.getPhone())
			.ne(OauthUser::getId, Optional.ofNullable(param.getId()).orElse(0L)))) {
			throw new DataExistsException("手机号已存在,请修改后重试!");
		}
		OauthUser oauthUser = oauthUserConverter.param2dto(param);
		if (Optional.ofNullable(param.getId()).orElse(0L) >= 1) {
			oauthUser.setUsername(null);
			oauthUser.setPassword(null);
		}
		else {
			oauthUser.setPassword(passwordEncryptService.encrypt(param.getPassword()));
		}
		super.saveOrUpdate(oauthUser);
		oauthUserPostService.batchSave(oauthUser.getId(), param.getPostIds());
		oauthUserRoleService.batchSave(oauthUser.getId(), param.getRoleIds());
		return oauthUser.getId();
	}

	@Override
	public OauthUserVO selectById(Long id) {
		OauthUser oauthUser = baseMapper.selectById(id);
		if (Optional.ofNullable(oauthUser).map(OauthUser::getId).orElse(0L) <= 0) {
			throw new AuthenticationException("用户不存在请重新登录");
		}
		OauthUserVO oauthUserVO = oauthUserConverter.dto2vo(oauthUser);
		OauthDept oauthDept = oauthDeptService.getById(oauthUser.getDeptId());
		if (Optional.ofNullable(oauthDept).map(OauthDept::getId).orElse(0L) >= 1) {
			oauthUserVO.setDeptName(oauthDept.getName());
		}
		List<Long> postIds = oauthUserPostService.listObjs(Wrappers.<OauthUserPost>lambdaQuery()
			.eq(OauthUserPost::getStatus, BooleanEnum.YES.getCode())
			.eq(OauthUserPost::getUserId, id)
			.select(OauthUserPost::getPostId));
		List<Long> roleIds = oauthUserRoleService.listObjs(Wrappers.<OauthUserRole>lambdaQuery()
			.eq(OauthUserRole::getStatus, BooleanEnum.YES.getCode())
			.eq(OauthUserRole::getUserId, id)
			.select(OauthUserRole::getRoleId));
		oauthUserVO.setPostIds(postIds);
		oauthUserVO.setRoleIds(roleIds);
		return oauthUserVO;
	}

	@Override
	public SecurityClaims findByUsername(String username) {
		LambdaQueryWrapper<OauthUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(OauthUser::getUsername, username);
		queryWrapper.orderByDesc(OauthUser::getUpdateAt);
		OauthUser oauthUser = baseMapper.selectOne(queryWrapper);
		if (Optional.ofNullable(oauthUser).map(OauthUser::getId).orElse(0L) <= 0) {
			throw new AuthenticationException("用户名不存在");
		}
		if (!Objects.equals(BooleanEnum.YES.getCode(), oauthUser.getStatus())) {
			throw new AuthenticationException("用户已禁用");
		}
		return SecurityClaims.builder()
			.id(oauthUser.getId())
			.username(oauthUser.getUsername())
			.nickName(oauthUser.getName())
			.build();
	}

	@Override
	public void changePassword(String oldPwd, String newPwd) {
		Long id = SecurityUtils.getId();
		String oldPassword = passwordEncryptService.encrypt(oldPwd);
		LambdaQueryWrapper<OauthUser> queryWrapper = Wrappers.<OauthUser>lambdaQuery()
			.eq(OauthUser::getId, id)
			.eq(OauthUser::getPassword, oldPassword);
		if (!exists(queryWrapper)) {
			throw new ServiceException("原始密码不匹配,请修改后重试!");
		}
		String newPassword = passwordEncryptService.encrypt(newPwd);
		LambdaUpdateWrapper<OauthUser> updateWrapper = Wrappers.<OauthUser>lambdaUpdate()
			.eq(OauthUser::getId, id)
			.set(OauthUser::getPassword, newPassword);
		boolean update = super.update(updateWrapper);
		if (!update) {
			throw new ServiceException("操作失败，请重试!");
		}
	}

	@Override
	public SecurityClaims login(String username, String password) {
		LambdaQueryWrapper<OauthUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(OauthUser::getUsername, username)
			.or()
			.eq(OauthUser::getEmail, username)
			.or()
			.eq(OauthUser::getPhone, username);
		queryWrapper.orderByDesc(OauthUser::getUpdateAt);
		OauthUser oauthUser = baseMapper.selectOne(queryWrapper);
		if (Optional.ofNullable(oauthUser).map(OauthUser::getId).orElse(0L) <= 0) {
			throw new ServiceException("用户名不存在");
		}
		if (!Objects.equals(BooleanEnum.YES.getCode(), oauthUser.getStatus())) {
			throw new ServiceException("用户已禁用");
		}
		if (!StringUtils.equalsIgnoreCase(oauthUser.getPassword(), passwordEncryptService.encrypt(password))) {
			throw new ServiceException("密码错误");
		}
		SecurityClaims claims = SecurityClaims.builder()
			.id(oauthUser.getId())
			.username(oauthUser.getUsername())
			.nickName(oauthUser.getName())
			.build();
		oauthUser.setLastLoginIp(IPUtils.getClientIp());
		oauthUser.setLastLoginTime(LocalDateTime.now());
		baseMapper.updateById(oauthUser);
		return claims;
	}

}
