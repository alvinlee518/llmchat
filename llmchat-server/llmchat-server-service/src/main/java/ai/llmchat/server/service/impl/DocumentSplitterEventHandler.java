package ai.llmchat.server.service.impl;

import ai.llmchat.common.langchain.document.Paragraph;
import ai.llmchat.common.langchain.document.ParagraphParserFactory;
import ai.llmchat.common.langchain.event.DisruptorConsumer;
import ai.llmchat.common.langchain.event.DocumentEvent;
import ai.llmchat.server.api.enums.IndexStateEnum;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.entity.FileDetail;
import ai.llmchat.server.repository.mapper.AiDocumentMapper;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import ai.llmchat.server.repository.mapper.FileDetailMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DocumentSplitterEventHandler implements DisruptorConsumer {
    private final AiDocumentMapper aiDocumentMapper;
    private final AiParagraphMapper aiParagraphMapper;
    private final FileDetailMapper fileDetailMapper;

    public DocumentSplitterEventHandler(AiDocumentMapper aiDocumentMapper, AiParagraphMapper aiParagraphMapper, FileDetailMapper fileDetailMapper) {
        this.aiDocumentMapper = aiDocumentMapper;
        this.aiParagraphMapper = aiParagraphMapper;
        this.fileDetailMapper = fileDetailMapper;
    }


    @Override
    public void onEvent(DocumentEvent documentEvent, long l, boolean b) throws Exception {
        AiDocument document = aiDocumentMapper.selectById(documentEvent.getDocId());
        if (Optional.ofNullable(document).map(AiDocument::getId).orElse(0L) <= 0) {
            return;
        }
        // 如果不是待处理或处理失败状态则跳过
        if (!(Objects.equals(IndexStateEnum.PENDING.getCode(), document.getIndexState())
                || Objects.equals(IndexStateEnum.FAILURE.getCode(), document.getIndexState()))) {
            return;
        }
        try {
            aiDocumentMapper.update(
                    Wrappers.<AiDocument>lambdaUpdate().eq(AiDocument::getId, document.getId())
                            .set(AiDocument::getIndexState, IndexStateEnum.IN_PROCESSING.getCode())
                            .set(AiDocument::getFailure, StringUtils.EMPTY)
            );
            FileDetail fileDetail = fileDetailMapper.selectById(document.getFileId());
            if (Optional.ofNullable(fileDetail).map(FileDetail::getId).orElse(0L) <= 0) {
                throw new RuntimeException("document file not exists");
            }
            List<Paragraph> list = ParagraphParserFactory.create(
                    document.getDataType(),
                    fileDetail.getUrl(),
                    document.getSeparators(),
                    document.getChunkSize(),
                    document.getChunkOverlap(),
                    document.getCleanRules()
            ).parse();
            List<AiParagraph> paragraphList = new ArrayList<>();
            for (Paragraph item : list) {
                AiParagraph paragraph = toParagraph(item, document);
                paragraphList.add(paragraph);
            }
            aiParagraphMapper.insert(paragraphList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            aiDocumentMapper.update(Wrappers.<AiDocument>lambdaUpdate()
                    .eq(AiDocument::getId, document.getId())
                    .set(AiDocument::getIndexState, IndexStateEnum.FAILURE.getCode())
                    .set(AiDocument::getFailure, e.getMessage())
            );
        }
    }

    private AiParagraph toParagraph(Paragraph paragraph, AiDocument document) {
        AiParagraph aiParagraph = new AiParagraph();
        aiParagraph.setDatasetId(document.getDatasetId());
        aiParagraph.setDocId(document.getId());
        aiParagraph.setTitle(paragraph.getPrompt());
        aiParagraph.setContent(paragraph.getCompletion());
        aiParagraph.setPosition(paragraph.getPosition());
        return aiParagraph;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
