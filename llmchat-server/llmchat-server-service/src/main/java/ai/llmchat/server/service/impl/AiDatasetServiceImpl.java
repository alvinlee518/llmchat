package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.langchain.enums.SearchModeEnum;
import ai.llmchat.common.langchain.rag.content.ContentSearchOptions;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import ai.llmchat.common.langchain.util.LangchainConstants;
import ai.llmchat.common.redis.core.MessageStreamPublisher;
import ai.llmchat.server.api.param.CommonPageParam;
import ai.llmchat.server.api.param.DatasetParam;
import ai.llmchat.server.api.param.HitTestingParam;
import ai.llmchat.server.api.vo.DatasetVO;
import ai.llmchat.server.api.vo.HitTestingVO;
import ai.llmchat.server.repository.dataobject.DatasetDO;
import ai.llmchat.server.repository.entity.AiDataset;
import ai.llmchat.server.repository.mapper.AiDatasetMapper;
import ai.llmchat.server.service.AiDatasetService;
import ai.llmchat.server.service.AiDocumentService;
import ai.llmchat.server.service.AiModelService;
import ai.llmchat.server.service.AiParagraphService;
import ai.llmchat.server.service.converter.AiDatasetConverter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 数据集 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Service
public class AiDatasetServiceImpl extends ServiceImpl<AiDatasetMapper, AiDataset> implements AiDatasetService {
    private final AiDatasetConverter aiDatasetConverter;
    private final ContentStore contentStore;
    private final AiModelService aiModelService;
    private final AiParagraphService aiParagraphService;

    public AiDatasetServiceImpl(AiDatasetConverter aiDatasetConverter,
                                ContentStore contentStore,
                                AiModelService aiModelService, AiParagraphService aiParagraphService) {
        this.aiDatasetConverter = aiDatasetConverter;
        this.contentStore = contentStore;
        this.aiModelService = aiModelService;
        this.aiParagraphService = aiParagraphService;
    }

    @Override
    public PageData<DatasetVO> queryPage(CommonPageParam param) {
        PageInfo<DatasetDO> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
                .doSelectPageInfo(() -> baseMapper.queryPage(param.getName()));
        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), aiDatasetConverter.do2vo(pageInfo.getList()));
    }

    @Override
    public Long saveOrUpdate(DatasetParam param) {
        AiDataset dataset = aiDatasetConverter.param2dto(param);
        if (Optional.ofNullable(param.getId()).orElse(0L) >= 1) {
            AiDataset aiDataset = baseMapper.selectById(param.getId());
            if (Optional.ofNullable(aiDataset).map(AiDataset::getId).orElse(0L) >= 1) {
                baseMapper.updateById(dataset);
                if (!Objects.equals(param.getEmbedId(), aiDataset.getEmbedId())) {
                    aiParagraphService.reindexByDatasetId(dataset.getId());
                }
                return dataset.getId();
            }
        }
        baseMapper.insert(dataset);
        return dataset.getId();
    }

    @Override
    public List<HitTestingVO> hitTesting(HitTestingParam param) {
        ContentSearchOptions.ContentSearchOptionsBuilder optionsBuilder = ContentSearchOptions.builder().keyword(param.getKeyword())
                .maxResults(param.getTopK())
                .minScore(param.getScore())
                .filter(MetadataFilterBuilder.metadataKey(LangchainConstants.METADATA_FIELD_DATASET).isEqualTo(param.getDatasetId()));
        List<EmbeddingMatch<TextSegment>> searchResult;
        if (Objects.equals(SearchModeEnum.SIMILARITY.getCode(), param.getSearchMode())) {
            EmbeddingModel embeddingModel = aiModelService.embeddingModelByDatasetId(param.getDatasetId());
            Embedding embedding = embeddingModel.embed(param.getKeyword()).content();
            searchResult = contentStore.similaritySearch(optionsBuilder.embedding(embedding).build());
        } else if (Objects.equals(SearchModeEnum.KEYWORD.getCode(), param.getSearchMode())) {
            searchResult = contentStore.keywordSearch(optionsBuilder.build());
        } else {
            EmbeddingModel embeddingModel = aiModelService.embeddingModelByDatasetId(param.getDatasetId());
            Embedding embeddedQuery = embeddingModel.embed(param.getKeyword()).content();
            searchResult = contentStore.hybridSearch(optionsBuilder.embedding(embeddedQuery).build());
        }
        List<HitTestingVO> result = new ArrayList<>();
        for (EmbeddingMatch<TextSegment> match : searchResult) {
            TextSegment embedded = match.embedded();
            Long paraId = embedded.metadata().getLong(LangchainConstants.METADATA_FIELD_PARAGRAPH);
            Double score = match.score();
            HitTestingVO build = HitTestingVO.builder().id(paraId).score(score).content(embedded.text()).build();
            result.add(build);
        }
        return result;
    }

    @Override
    public List<AiDataset> listByAppId(Long appId) {
        return baseMapper.listByAppId(appId);
    }
}
