package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.langchain.event.DisruptorConsumer;
import ai.llmchat.common.langchain.event.DocumentEvent;
import ai.llmchat.server.api.enums.IndexStateEnum;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.mapper.AiDocumentMapper;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DocumentCompletionEventHandler implements DisruptorConsumer {
    private final AiDocumentMapper aiDocumentMapper;
    private final AiParagraphMapper aiParagraphMapper;

    public DocumentCompletionEventHandler(AiDocumentMapper aiDocumentMapper, AiParagraphMapper aiParagraphMapper) {
        this.aiDocumentMapper = aiDocumentMapper;
        this.aiParagraphMapper = aiParagraphMapper;
    }

    @Override
    public void onEvent(DocumentEvent documentEvent, long l, boolean b) throws Exception {
        LambdaQueryWrapper<AiParagraph> queryWrapper = Wrappers.<AiParagraph>lambdaQuery()
                .eq(AiParagraph::getStatus, BooleanEnum.YES.getCode())
                .eq(AiParagraph::getDocId, documentEvent.getDocId())
                .select(AiParagraph::getIndexState);
        List<Integer> list = aiParagraphMapper.selectObjs(queryWrapper);
        if (CollectionUtils.isNotEmpty(list) && list.stream().allMatch(item -> Objects.equals(IndexStateEnum.COMPLETION.getCode(), item))) {
            LambdaUpdateWrapper<AiDocument> updateWrapper = Wrappers.<AiDocument>lambdaUpdate()
                    .eq(AiDocument::getId, documentEvent.getDocId())
                    .set(AiDocument::getIndexState, IndexStateEnum.COMPLETION.getCode())
                    .set(AiDocument::getFailure, StringUtils.EMPTY);
            aiDocumentMapper.update(updateWrapper);
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
