package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectGroupNode;
import ai.llmchat.server.api.param.ModelPageParam;
import ai.llmchat.server.api.vo.ModelProviderVO;
import ai.llmchat.server.api.vo.ModelVO;
import ai.llmchat.server.repository.dataobject.ModelOptionsDO;
import ai.llmchat.server.repository.entity.AiModel;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.langchain4j.model.embedding.EmbeddingModel;

import java.util.List;

/**
 * <p>
 * 模型配置 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-24
 */
public interface

AiModelService extends IService<AiModel> {
    PageData<ModelVO> queryPage(ModelPageParam param);

    List<SelectGroupNode> selectGroupOptions(Integer modelType);

    List<ModelProviderVO> modelProviderList();

    EmbeddingModel embeddingModelByDatasetId(Long datasetId);

    ModelOptionsDO findByChatId(Long chatId);
}
