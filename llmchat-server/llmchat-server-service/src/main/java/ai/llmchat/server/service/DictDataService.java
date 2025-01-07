package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.repository.entity.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-22
 */
public interface DictDataService extends IService<DictData> {

	PageData<DictData> queryPage(CommonPageParam param);

}
