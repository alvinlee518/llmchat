package ai.llmchat.server.service.impl;

import ai.llmchat.server.repository.entity.OauthUserPost;
import ai.llmchat.server.repository.mapper.OauthUserPostMapper;
import ai.llmchat.server.service.OauthUserPostService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 用户岗位关联表 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-23
 */
@Service
public class OauthUserPostServiceImpl extends ServiceImpl<OauthUserPostMapper, OauthUserPost> implements OauthUserPostService {

    @Override
    public void batchSave(Long userId, List<Long> ids) {
        remove(Wrappers.<OauthUserPost>lambdaQuery().eq(OauthUserPost::getUserId, userId));
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<OauthUserPost> list = ids.stream()
                .map(item -> {
                    OauthUserPost result = new OauthUserPost();
                    result.setPostId(item);
                    result.setUserId(userId);
                    return result;
                }).toList();
        saveBatch(list);
    }
}
