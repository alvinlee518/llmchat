package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.param.DataScopeParam;
import ai.llmchat.server.api.param.MenuScopeParam;
import ai.llmchat.server.api.vo.OauthRoleVO;
import ai.llmchat.server.repository.entity.OauthRole;
import ai.llmchat.server.repository.entity.OauthRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
public interface OauthRoleService extends IService<OauthRole> {

	PageData<OauthRole> queryPage(CommonPageParam param);

	DataScopeParam dataScopeById(Long id);

	void modifyDataScope(DataScopeParam param);

	MenuScopeParam menuScopeById(Long id);

	void modifyMenuScope(MenuScopeParam param);

	List<SelectNode> selectOptions();

}
