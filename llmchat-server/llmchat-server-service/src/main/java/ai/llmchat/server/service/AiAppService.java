package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.param.AppParam;
import ai.llmchat.server.api.vo.AppVO;
import ai.llmchat.server.repository.entity.AiApp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 应用 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
public interface AiAppService extends IService<AiApp> {

	PageData<AppVO> queryPage(CommonPageParam param);

	AppVO findById(Long appId);

	Long saveOrUpdate(AppParam param);

}
