package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.core.wrapper.data.SelectGroupNode;
import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.model.ModelProvider;
import ai.llmchat.common.langchain.model.ModelProviderFactory;
import ai.llmchat.common.langchain.model.options.EmbeddingModelOptions;
import ai.llmchat.common.langchain.model.options.ModelOptions;
import ai.llmchat.common.langchain.util.LangchainConstants;
import ai.llmchat.server.api.param.ModelPageParam;
import ai.llmchat.server.api.vo.ModelProviderVO;
import ai.llmchat.server.api.vo.ModelVO;
import ai.llmchat.server.api.vo.SelectOption;
import ai.llmchat.server.repository.dataobject.ModelOptionsDO;
import ai.llmchat.server.repository.entity.AiModel;
import ai.llmchat.server.repository.mapper.AiModelMapper;
import ai.llmchat.server.service.AiModelService;
import ai.llmchat.server.service.converter.AiModelsConverter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 模型配置 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-24
 */
@Service
public class AiModelServiceImpl extends ServiceImpl<AiModelMapper, AiModel> implements AiModelService {
    private final ModelProviderFactory modelProviderFactory;
    private final AiModelsConverter aiModelsConverter;

    public AiModelServiceImpl(ModelProviderFactory modelProviderFactory, AiModelsConverter aiModelsConverter) {
        this.modelProviderFactory = modelProviderFactory;
        this.aiModelsConverter = aiModelsConverter;
    }

    @Override
    public PageData<ModelVO> queryPage(ModelPageParam param) {
        LambdaQueryWrapper<AiModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, AiModel::getStatus, param.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(param.getModelName()), AiModel::getModelName, param.getModelName());
        queryWrapper.eq(Optional.ofNullable(param.getModelProvider()).orElse(0) >= 1, AiModel::getModelProvider, param.getModelProvider());
        queryWrapper.orderByDesc(AiModel::getUpdateAt);
        PageInfo<AiModel> pageInfo = PageHelper.startPage(param.getPage(), param.getSize()).doSelectPageInfo(() -> this.list(queryWrapper));

        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), aiModelsConverter.dto2vo(pageInfo.getList()));
    }

    @Override
    public boolean saveOrUpdate(AiModel entity) {
        ModelOptions options = new ModelOptions();
        options.setModelProvider(entity.getModelProvider());
        options.setModelName(entity.getModelName());
        options.setModelType(entity.getModelType());
        options.setBaseUrl(entity.getBaseUrl());
        options.setApiKey(entity.getApiKey());
        options.setSecretKey(entity.getSecretKey());
        modelProviderFactory.credentialsValidate(options);

        return super.saveOrUpdate(entity);
    }

    @Override
    public List<SelectGroupNode> selectGroupOptions(Integer modelType) {
        LambdaQueryWrapper<AiModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiModel::getStatus, BooleanEnum.YES.getCode());
        queryWrapper.eq(AiModel::getModelType, modelType);
        queryWrapper.select(AiModel::getId, AiModel::getModelName, AiModel::getModelProvider);
        List<AiModel> list = this.list(queryWrapper);
        List<SelectGroupNode> result = new ArrayList<>();
        for (ModelProviderEnum providerEnum : ModelProviderEnum.values()) {
            long longValue = providerEnum.getCode().longValue();
            List<SelectGroupNode> children = list.stream().filter(item -> Objects.equals(item.getModelProvider(), providerEnum.getCode())).map(item -> new SelectGroupNode(item.getModelName(), item.getId())).toList();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            SelectGroupNode node = new SelectGroupNode(providerEnum.getMessage(), longValue);
            node.setType("group");
            node.setChildren(children);
            result.add(node);
        }
        return result;
    }

    @Override
    public List<ModelProviderVO> modelProviderList() {
        List<ModelProviderVO> list = new ArrayList<>();
        List<ModelProvider> modelProviders = modelProviderFactory.getModelProviders();
        for (ModelProvider modelProvider : modelProviders) {
            List<SelectOption> modelTypes = modelProvider.supportedModelTypes().stream().map(item -> new SelectOption(item.getMessage(), item.getCode())).toList();
            if (CollectionUtils.isEmpty(modelTypes)) {
                continue;
            }
            ModelProviderEnum definition = modelProvider.modelProvider();
            ModelProviderVO modelProviderVO = new ModelProviderVO();
            modelProviderVO.setIcon(definition.getIcon());
            modelProviderVO.setSupportedModelTypes(modelTypes);
            modelProviderVO.setLabel(definition.getMessage());
            modelProviderVO.setValue(definition.getCode());
            list.add(modelProviderVO);
        }
        return list;
    }

    @Override
    public EmbeddingModel embeddingModelByDatasetId(Long datasetId) {
        ModelOptionsDO model = baseMapper.getModelByDatasetId(datasetId);
        if (Optional.ofNullable(model).map(ModelOptionsDO::getId).orElse(0L) <= 0) {
            throw new RuntimeException("can not find model by dataset:" + datasetId);
        }
        EmbeddingModelOptions modelOptions = new EmbeddingModelOptions();
        modelOptions.setDimensions(LangchainConstants.DIMENSIONS);
        modelOptions.setModelProvider(model.getModelProvider());
        modelOptions.setModelName(model.getModelName());
        modelOptions.setModelType(model.getModelType());
        modelOptions.setBaseUrl(model.getBaseUrl());
        modelOptions.setApiKey(model.getApiKey());
        modelOptions.setSecretKey(model.getSecretKey());
        return modelProviderFactory.embeddingModel(modelOptions);
    }

    @Override
    public ModelOptionsDO findByChatId(Long chatId) {
        ModelOptionsDO model = baseMapper.getModelByChatId(chatId);
        if (Optional.ofNullable(model).map(ModelOptionsDO::getId).orElse(0L) <= 0) {
            throw new RuntimeException("can not find model by chatId:" + chatId);
        }
        return model;
    }

    @Override
    public AiModel getById(Serializable id) {
        AiModel model = super.getById(id);
        if (Optional.ofNullable(model).map(AiModel::getId).orElse(0L) <= 0) {
            throw new RuntimeException("can not find model by modelId:" + id);
        }
        return model;
    }
}
