package ai.llmchat.server.service.consumer;

import ai.llmchat.common.langchain.rag.content.ContentStoreIngestor;
import ai.llmchat.common.langchain.rag.content.IngestionResult;
import ai.llmchat.common.langchain.util.LangchainConstants;
import ai.llmchat.common.redis.core.Action;
import ai.llmchat.common.redis.core.Message;
import ai.llmchat.common.redis.core.MessageStreamListener;
import ai.llmchat.server.api.enums.StateEnum;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.service.AiDocumentService;
import ai.llmchat.server.service.AiModelService;
import ai.llmchat.server.service.AiParagraphService;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EmbeddingConsumer implements MessageStreamListener {
    private final AiModelService aiModelService;
    private final AiDocumentService aiDocumentService;
    private final AiParagraphService aiParagraphService;
    private final ContentStoreIngestor contentStoreIngestor;

    public EmbeddingConsumer(AiModelService aiModelService, AiDocumentService aiDocumentService, AiParagraphService aiParagraphService, ContentStoreIngestor contentStoreIngestor) {
        this.aiModelService = aiModelService;
        this.aiDocumentService = aiDocumentService;
        this.aiParagraphService = aiParagraphService;
        this.contentStoreIngestor = contentStoreIngestor;
    }

    @Override
    public Action doConsume(Message message) {
        long paraId = Long.parseLong(message.getBody());
        AiParagraph paragraph = aiParagraphService.getById(paraId);
        if (Optional.ofNullable(paragraph).map(AiParagraph::getId).orElse(0L) <= 0) {
            return Action.CommitMessage;
        }
        AiDocument document = aiDocumentService.getById(paragraph.getDocId());
        if (Optional.ofNullable(document).map(AiDocument::getId).orElse(0L) <= 0) {
            return Action.CommitMessage;
        }

        aiParagraphService.changeState(List.of(paragraph.getId()), StateEnum.IN_PROCESSING);
        List<String> texts = new ArrayList<>();
        if ((document.getEmbedCols() & 1) == 1) {
            texts.add(paragraph.getTitle());
        }
        if ((document.getEmbedCols() & 2) == 2) {
            texts.add(paragraph.getContent());
        }
        Map<String, Object> metadata = new HashMap<>() {
            {
                put(LangchainConstants.METADATA_FIELD_DATASET, paragraph.getDatasetId());
                put(LangchainConstants.METADATA_FIELD_DOCUMENT, paragraph.getDocId());
                put(LangchainConstants.METADATA_FIELD_PARAGRAPH, paragraph.getId());
            }
        };
        TextSegment segment = TextSegment.from(StringUtils.join(texts, System.lineSeparator()), Metadata.from(metadata));
        EmbeddingModel embeddingModel = aiModelService.embeddingModelByDatasetId(document.getDatasetId());
        IngestionResult ingestionResult = contentStoreIngestor.ingest(embeddingModel, List.of(segment));
        List<String> embedIdList = ingestionResult.getEmbedIdList();
        paragraph.setIndexId(embedIdList.get(0));
        paragraph.setState(StateEnum.COMPLETION.getCode());
        paragraph.setFailure(StringUtils.EMPTY);
        aiParagraphService.updateById(paragraph);
        return Action.CommitMessage;
    }

    @Override
    public String getGroup() {
        return "GROUP_PARAGRAPH_EMBEDDING";
    }

    @Override
    public String getTopic() {
        return MessageConstants.TOPIC_PARAGRAPH_EMBEDDING;
    }
}
