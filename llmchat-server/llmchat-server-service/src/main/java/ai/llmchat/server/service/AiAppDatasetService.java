package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.AppDatasetParam;
import ai.llmchat.server.api.vo.AppDatasetVO;
import ai.llmchat.server.api.vo.DatasetItemVO;
import ai.llmchat.server.repository.entity.AiAppDataset;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 应用数据集关联表 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
public interface AiAppDatasetService extends IService<AiAppDataset> {
    PageData<AppDatasetVO> queryPage(AppDatasetParam param);

    void batchSave(Long appId, List<Long> datasetIds);

    List<DatasetItemVO> listByAppId(Long appId);
}
