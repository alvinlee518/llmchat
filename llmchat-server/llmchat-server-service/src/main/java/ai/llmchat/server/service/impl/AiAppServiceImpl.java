package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.param.AppParam;
import ai.llmchat.server.api.vo.AppVO;
import ai.llmchat.server.api.vo.DatasetItemVO;
import ai.llmchat.server.repository.entity.AiApp;
import ai.llmchat.server.repository.entity.AiModel;
import ai.llmchat.server.repository.mapper.AiAppMapper;
import ai.llmchat.server.repository.mapper.AiModelMapper;
import ai.llmchat.server.service.AiAppDatasetService;
import ai.llmchat.server.service.AiAppService;
import ai.llmchat.server.service.converter.AiAppConverter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 应用 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
@Service
public class AiAppServiceImpl extends ServiceImpl<AiAppMapper, AiApp> implements AiAppService {
    private final AiModelMapper aiModelMapper;
    private final AiAppConverter aiAppConverter;
    private final AiAppDatasetService aiAppDatasetService;

    public AiAppServiceImpl(AiModelMapper aiModelMapper, AiAppConverter aiAppConverter, AiAppDatasetService aiAppDatasetService) {
        this.aiModelMapper = aiModelMapper;
        this.aiAppConverter = aiAppConverter;
        this.aiAppDatasetService = aiAppDatasetService;
    }


    @Override
    public PageData<AppVO> queryPage(CommonPageParam param) {
        LambdaQueryWrapper<AiApp> queryWrapper = Wrappers.<AiApp>lambdaQuery().eq(StringUtils.isNotBlank(param.getName()), AiApp::getName, param.getName()).eq(Optional.ofNullable(param.getStatus()).orElse(-1) >= 0, AiApp::getStatus, param.getStatus()).orderByDesc(AiApp::getUpdateAt);
        PageInfo<AiApp> pageInfo = PageHelper.startPage(param.getPage(), param.getSize()).doSelectPageInfo(() -> list(queryWrapper));
        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), aiAppConverter.do2vo(pageInfo.getList()));
    }

    @Override
    public AppVO findById(Long appId) {
        AiApp app = this.getById(appId);
        if (Optional.ofNullable(app).map(AiApp::getId).orElse(0L) <= 0) {
            return null;
        }
        AppVO result = aiAppConverter.do2vo(app);
        if (Optional.ofNullable(app.getModelId()).orElse(0L) >= 1) {
            AiModel model = aiModelMapper.selectById(app.getModelId());
            if (Optional.ofNullable(model).map(AiModel::getId).orElse(0L) >= 1) {
                result.setModelId(app.getModelId());
                result.setModelName(model.getModelName());
            }
        }
        if (Optional.ofNullable(app.getRerankId()).orElse(0L) >= 1) {
            AiModel model = aiModelMapper.selectById(app.getRerankId());
            if (Optional.ofNullable(model).map(AiModel::getId).orElse(0L) >= 1) {
                result.setRerankId(app.getModelId());
                result.setRerankModelName(model.getModelName());
            }
        }
        List<DatasetItemVO> datasetList = aiAppDatasetService.listByAppId(app.getId());
        result.setDatasets(Optional.ofNullable(datasetList).orElse(new ArrayList<>()));
        return result;
    }

    @Override
    public Long saveOrUpdate(AppParam param) {
        AiApp app = aiAppConverter.param2do(param);
        super.saveOrUpdate(app);
        aiAppDatasetService.batchSave(app.getId(), Optional.ofNullable(param.getDatasets()).orElse(new ArrayList<>()).stream().map(DatasetItemVO::getId).toList());
        return app.getId();
    }
}
