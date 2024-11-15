package ai.llmchat.server.service;

import ai.llmchat.server.repository.entity.OauthRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单关联表 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
public interface OauthRoleMenuService extends IService<OauthRoleMenu> {
    void batchSave(Long roleId, List<Long> ids);
}
