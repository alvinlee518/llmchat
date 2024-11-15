package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectNode;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.OauthPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 岗位 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
public interface OauthPostService extends IService<OauthPost> {

    PageData<OauthPost> queryPage(CommonPageParam param);

    List<SelectNode> selectOptions();
}
