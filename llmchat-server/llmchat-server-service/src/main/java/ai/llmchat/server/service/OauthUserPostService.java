package ai.llmchat.server.service;

import ai.llmchat.server.repository.entity.OauthUserPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户岗位关联表 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
public interface OauthUserPostService extends IService<OauthUserPost> {

	void batchSave(Long userId, List<Long> ids);

}
