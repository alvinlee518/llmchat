package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.param.AppDatasetParam;
import ai.llmchat.server.api.vo.AppDatasetVO;
import ai.llmchat.server.api.vo.DatasetItemVO;
import ai.llmchat.server.repository.dataobject.AppDatasetDO;
import ai.llmchat.server.repository.dataobject.DatasetItemDO;
import ai.llmchat.server.repository.entity.AiAppDataset;
import ai.llmchat.server.repository.mapper.AiAppDatasetMapper;
import ai.llmchat.server.service.AiAppDatasetService;
import ai.llmchat.server.service.converter.AiAppDatasetConverter;
import ai.llmchat.server.service.converter.AiDatasetConverter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 应用数据集关联表 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-11-04
 */
@Service
public class AiAppDatasetServiceImpl extends ServiceImpl<AiAppDatasetMapper, AiAppDataset>
		implements AiAppDatasetService {

	private final AiAppDatasetConverter aiAppDatasetConverter;

	public AiAppDatasetServiceImpl(AiAppDatasetConverter aiAppDatasetConverter) {
		this.aiAppDatasetConverter = aiAppDatasetConverter;
	}

	@Override
	public PageData<AppDatasetVO> queryPage(AppDatasetParam param) {
		PageInfo<AppDatasetDO> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> baseMapper.queryPage(param.getAppId(), param.getKeyword()));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(),
				aiAppDatasetConverter.do2vo(pageInfo.getList()));
	}

	@Override
	public void batchSave(Long appId, List<Long> datasetIds) {
		LambdaQueryWrapper<AiAppDataset> queryWrapper = Wrappers.<AiAppDataset>lambdaQuery()
			.eq(AiAppDataset::getAppId, appId);
		baseMapper.delete(queryWrapper);
		if (CollectionUtils.isNotEmpty(datasetIds)) {
			List<AiAppDataset> list = datasetIds.stream().map(item -> {
				AiAppDataset dataset = new AiAppDataset();
				dataset.setAppId(appId);
				dataset.setDatasetId(item);
				return dataset;
			}).toList();
			baseMapper.insert(list);
		}
	}

	@Override
	public List<DatasetItemVO> listByAppId(Long appId) {
		List<DatasetItemDO> list = baseMapper.listByAppId(appId);
		return aiAppDatasetConverter.itemDo2vo(list);
	}

}
