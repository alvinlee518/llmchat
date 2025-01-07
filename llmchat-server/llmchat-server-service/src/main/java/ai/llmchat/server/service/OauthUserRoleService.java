package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.UserRolePageParam;
import ai.llmchat.server.api.param.UserRoleParam;
import ai.llmchat.server.repository.entity.OauthUser;
import ai.llmchat.server.repository.entity.OauthUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
public interface OauthUserRoleService extends IService<OauthUserRole> {

	void batchSave(Long userId, List<Long> ids);

	PageData<OauthUser> userRoleList(UserRolePageParam param);

	void allocated(UserRoleParam param);

	void unallocated(UserRoleParam param);

}
