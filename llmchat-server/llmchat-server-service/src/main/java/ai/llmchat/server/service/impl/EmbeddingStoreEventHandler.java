package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.langchain.event.DisruptorConsumer;
import ai.llmchat.common.langchain.event.DocumentEvent;
import ai.llmchat.common.langchain.rag.content.ContentStore;
import ai.llmchat.common.langchain.util.LangchainConstants;
import ai.llmchat.server.api.enums.IndexStateEnum;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.mapper.AiDocumentMapper;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import ai.llmchat.server.service.AiModelService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hankcs.hanlp.HanLP;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsIn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EmbeddingStoreEventHandler implements DisruptorConsumer {
    private final AiDocumentMapper aiDocumentMapper;
    private final AiParagraphMapper aiParagraphMapper;
    private final AiModelService aiModelService;
    private final ContentStore contentStore;

    public EmbeddingStoreEventHandler(AiDocumentMapper aiDocumentMapper, AiParagraphMapper aiParagraphMapper, AiModelService aiModelService, ContentStore contentStore) {
        this.aiDocumentMapper = aiDocumentMapper;
        this.aiParagraphMapper = aiParagraphMapper;
        this.aiModelService = aiModelService;
        this.contentStore = contentStore;
    }

    @Override
    public void onEvent(DocumentEvent documentEvent, long l, boolean b) throws Exception {
        AiDocument document = aiDocumentMapper.selectById(documentEvent.getDocId());
        if (Optional.ofNullable(document).map(AiDocument::getId).orElse(0L) <= 0) {
            return;
        }
        if (!Objects.equals(document.getIndexState(), IndexStateEnum.IN_PROCESSING.getCode())) {
            return;
        }
        try {
            do {
                List<AiParagraph> list = aiParagraphMapper.selectList(Wrappers.<AiParagraph>lambdaQuery()
                        .eq(AiParagraph::getStatus, BooleanEnum.YES.getCode())
                        .eq(AiParagraph::getDocId, document.getId())
                        .eq(AiParagraph::getIndexState, IndexStateEnum.PENDING.getCode())
                        .last("limit 10")
                );
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                Filter filter = new IsEqualTo(LangchainConstants.METADATA_FIELD_DOCUMENT, document.getId())
                        .and(new IsIn(LangchainConstants.METADATA_FIELD_PARAGRAPH, list.stream().map(AiParagraph::getId).toList()));
                contentStore.removeAll(filter);
                List<Long> longList = list.stream().map(AiParagraph::getId).toList();
                try {
                    aiParagraphMapper.update(Wrappers.<AiParagraph>lambdaUpdate()
                            .set(AiParagraph::getIndexState, IndexStateEnum.IN_PROCESSING.getCode())
                            .set(AiParagraph::getFailure, StringUtils.EMPTY)
                            .in(AiParagraph::getId, longList));
                    EmbeddingModel embeddingModel = aiModelService.embeddingModelByDatasetId(document.getDatasetId());
                    List<TextSegment> segmentList = list.stream().map(item -> {
                        List<String> contents = new ArrayList<>();
                        if ((document.getEmbedCols() & 1) == 1) {
                            contents.add(item.getTitle());
                        }
                        if ((document.getEmbedCols() & 2) == 2) {
                            contents.add(item.getContent());
                        }
                        Map<String, Object> metadata = new HashMap<>() {
                            {
                                put(LangchainConstants.METADATA_FIELD_DATASET, document.getDatasetId());
                                put(LangchainConstants.METADATA_FIELD_DOCUMENT, document.getId());
                                put(LangchainConstants.METADATA_FIELD_PARAGRAPH, item.getId());
                            }
                        };
                        return TextSegment.from(StringUtils.join(contents, System.lineSeparator()), Metadata.from(metadata));
                    }).toList();
                    List<Embedding> embeddingList = embeddingModel.embedAll(segmentList).content();
                    List<String> embedIdList = contentStore.addAll(embeddingList, segmentList);
                    for (int i = 0; i < embedIdList.size(); i++) {
                        AiParagraph paragraph = list.get(i);
                        paragraph.setIndexId(embedIdList.get(i));
                        paragraph.setIndexState(IndexStateEnum.COMPLETION.getCode());
                        paragraph.setFailure(StringUtils.EMPTY);
                    }
                    aiParagraphMapper.updateById(list);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    aiParagraphMapper.update(Wrappers.<AiParagraph>lambdaUpdate()
                            .set(AiParagraph::getIndexState, IndexStateEnum.FAILURE.getCode())
                            .set(AiParagraph::getFailure, e.getMessage())
                            .in(AiParagraph::getId, longList));
                }
            } while (true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            aiDocumentMapper.update(Wrappers.<AiDocument>lambdaUpdate()
                    .eq(AiDocument::getId, document.getId())
                    .set(AiDocument::getIndexState, IndexStateEnum.FAILURE.getCode())
                    .set(AiDocument::getFailure, e.getMessage())
            );
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
