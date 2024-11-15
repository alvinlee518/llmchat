package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.OauthUserParam;
import ai.llmchat.server.api.param.OauthUserPageParam;
import ai.llmchat.server.api.vo.OauthUserVO;
import ai.llmchat.server.repository.entity.OauthUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lxw
 * @description 针对表【oauth_user】的数据库操作Service
 * @createDate 2024-10-21 14:15:53
 */
public interface OauthUserService extends IService<OauthUser> {
    PageData<OauthUserVO> queryPage(OauthUserPageParam param);

    Long saveOrUpdate(OauthUserParam param);

    OauthUserVO selectById(Long id);
}
