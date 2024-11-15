package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.exception.DataExistsException;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.param.DataScopeParam;
import ai.llmchat.server.api.param.MenuScopeParam;
import ai.llmchat.server.repository.entity.OauthRole;
import ai.llmchat.server.repository.entity.OauthRoleDept;
import ai.llmchat.server.repository.entity.OauthRoleMenu;
import ai.llmchat.server.repository.mapper.OauthRoleMapper;
import ai.llmchat.server.service.OauthRoleDeptService;
import ai.llmchat.server.service.OauthRoleMenuService;
import ai.llmchat.server.service.OauthRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
 * 角色 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
@Service
public class OauthRoleServiceImpl extends ServiceImpl<OauthRoleMapper, OauthRole> implements OauthRoleService {
    private final OauthRoleDeptService oauthRoleDeptService;
    private final OauthRoleMenuService oauthRoleMenuService;

    public OauthRoleServiceImpl(OauthRoleDeptService oauthRoleDeptService, OauthRoleMenuService oauthRoleMenuService) {
        this.oauthRoleDeptService = oauthRoleDeptService;
        this.oauthRoleMenuService = oauthRoleMenuService;
    }

    @Override
    public PageData<OauthRole> queryPage(CommonPageParam param) {
        LambdaQueryWrapper<OauthRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, OauthRole::getStatus, param.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(param.getName()), OauthRole::getName, param.getName());
        queryWrapper.orderByDesc(OauthRole::getUpdateAt);
        PageInfo<OauthRole> pageInfo = PageHelper.startPage(param.getPage(), param.getSize()).doSelectPageInfo(() -> this.list(queryWrapper));
        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
    }

    @Override
    public DataScopeParam dataScopeById(Long id) {
        OauthRole data = super.getById(id);
        List<Long> deptIds = oauthRoleDeptService.listObjs(Wrappers.<OauthRoleDept>lambdaQuery()
                .eq(OauthRoleDept::getStatus, BooleanEnum.YES.getCode())
                .eq(OauthRoleDept::getRoleId, id).select(OauthRoleDept::getDeptId));
        DataScopeParam param = new DataScopeParam();
        param.setId(data.getId());
        param.setName(data.getName());
        param.setCode(data.getCode());
        param.setDataScope(data.getDataScope());
        param.setDeptIds(deptIds);
        return param;
    }

    @Override
    public void modifyDataScope(DataScopeParam param) {
        LambdaUpdateWrapper<OauthRole> updateWrapper = Wrappers.<OauthRole>lambdaUpdate()
                .eq(OauthRole::getId, param.getId())
                .set(OauthRole::getDataScope, param.getDataScope());
        update(updateWrapper);
        oauthRoleDeptService.batchSave(param.getId(), param.getDeptIds());
    }

    @Override
    public MenuScopeParam menuScopeById(Long id) {
        OauthRole data = super.getById(id);
        List<Long> menuIds = oauthRoleMenuService.listObjs(Wrappers.<OauthRoleMenu>lambdaQuery()
                .eq(OauthRoleMenu::getStatus, BooleanEnum.YES.getCode())
                .eq(OauthRoleMenu::getRoleId, id).select(OauthRoleMenu::getMenuId));
        MenuScopeParam param = new MenuScopeParam();
        param.setId(data.getId());
        param.setName(data.getName());
        param.setCode(data.getCode());
        param.setMenuIds(menuIds);
        return param;
    }

    @Override
    public void modifyMenuScope(MenuScopeParam param) {
        oauthRoleMenuService.batchSave(param.getId(), param.getMenuIds());
    }

    @Override
    public List<SelectNode> selectOptions() {
        LambdaQueryWrapper<OauthRole> queryWrapper = Wrappers.<OauthRole>lambdaQuery()
                .eq(OauthRole::getStatus, BooleanEnum.YES.getCode())
                .select(OauthRole::getId, OauthRole::getName)
                .orderByDesc(OauthRole::getSorting);
        return list(queryWrapper).stream().map(item -> new SelectNode(item.getName(), item.getId())).toList();
    }

    @Override
    public boolean saveOrUpdate(OauthRole param) {
        LambdaQueryWrapper<OauthRole> queryWrapper = Wrappers.<OauthRole>lambdaQuery()
                .eq(OauthRole::getCode, param.getCode())
                .ne(OauthRole::getId, Optional.ofNullable(param.getId()).orElse(0L));
        if (exists(queryWrapper)) {
            throw new DataExistsException("角色编码已存在,请修改后重试!");
        }
        return super.saveOrUpdate(param);
    }
}
