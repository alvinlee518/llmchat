package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.langchain.document.Paragraph;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.param.DatasetParam;
import ai.llmchat.server.api.param.HitTestingParam;
import ai.llmchat.server.api.vo.DatasetVO;
import ai.llmchat.server.api.vo.HitTestingVO;
import ai.llmchat.server.repository.entity.AiDataset;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据集 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
public interface AiDatasetService extends IService<AiDataset> {
    PageData<DatasetVO> queryPage(CommonPageParam param);

    Long saveOrUpdate(DatasetParam param);

    List<HitTestingVO> hitTesting(HitTestingParam param);

    List<AiDataset> listByAppId(Long appId);
}
