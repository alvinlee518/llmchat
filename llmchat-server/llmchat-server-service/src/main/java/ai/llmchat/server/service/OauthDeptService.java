package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.TreeNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
public interface OauthDeptService extends IService<OauthDept> {

	PageData<OauthDept> queryPage(CommonPageParam param);

	List<TreeNode> treeData(Long parentId);

}
