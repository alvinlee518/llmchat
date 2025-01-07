package ai.llmchat.server.service.consumer;

import ai.llmchat.common.langchain.rag.loader.TextSegmentLoader;
import ai.llmchat.common.redis.core.Action;
import ai.llmchat.common.redis.core.Message;
import ai.llmchat.common.redis.core.MessageStreamListener;
import ai.llmchat.common.redis.core.MessageStreamPublisher;
import ai.llmchat.server.api.enums.StateEnum;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.entity.FileDetail;
import ai.llmchat.server.service.AiDocumentService;
import ai.llmchat.server.service.AiParagraphService;
import ai.llmchat.server.service.FileDetailService;
import dev.langchain4j.data.segment.TextSegment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class DocumentConsumer implements MessageStreamListener {

	private final AiDocumentService aiDocumentService;

	private final FileDetailService fileDetailService;

	private final AiParagraphService aiParagraphService;

	private final MessageStreamPublisher messageStreamPublisher;

	public DocumentConsumer(AiDocumentService aiDocumentService, FileDetailService fileDetailService,
			AiParagraphService aiParagraphService, MessageStreamPublisher messageStreamPublisher) {
		this.aiDocumentService = aiDocumentService;
		this.fileDetailService = fileDetailService;
		this.aiParagraphService = aiParagraphService;
		this.messageStreamPublisher = messageStreamPublisher;
	}

	@Override
	public Action doConsume(Message message) {
		Long docId = Long.parseLong(message.getBody());
		AiDocument document = aiDocumentService.getById(docId);
		if (Optional.ofNullable(document).map(AiDocument::getId).orElse(0L) <= 0) {
			return Action.CommitMessage;
		}
		// 如果不是待处理或处理失败状态则跳过
		if (!(Objects.equals(StateEnum.PENDING.getCode(), document.getState())
				|| Objects.equals(StateEnum.FAILURE.getCode(), document.getState()))) {
			return Action.CommitMessage;
		}
		try {
			aiDocumentService.changeState(document.getId(), StateEnum.IN_PROCESSING, StringUtils.EMPTY);
			FileDetail fileDetail = fileDetailService.getById(document.getFileId());
			if (Optional.ofNullable(fileDetail).map(FileDetail::getId).orElse(0L) <= 0) {
				throw new RuntimeException("document file not exists");
			}
			// 清理历史数据
			aiParagraphService.removeByDocId(document.getId());
			// 加载分段信息
			List<TextSegment> segmentList = TextSegmentLoader.builder()
				.dataType(document.getDataType())
				.url(fileDetail.getUrl())
				.separators(document.getSeparators())
				.size(document.getChunkSize())
				.overlap(document.getChunkOverlap())
				.cleanPatterns(document.getCleanRules())
				.build()
				.load();
			// 保存分段信息
			List<AiParagraph> list = segmentList.stream().map(item -> {
				AiParagraph paragraph = new AiParagraph();
				paragraph.setDatasetId(document.getDatasetId());
				paragraph.setDocId(document.getId());
				paragraph.setTitle(item.metadata().getString(TextSegmentLoader.PROMPT));
				paragraph.setContent(item.text());
				paragraph.setPosition(item.metadata().getInteger(TextSegmentLoader.INDEX));
				paragraph.setWordCount(StringUtils.length(item.text()));
				paragraph.setState(StateEnum.PENDING.getCode());
				return paragraph;
			}).toList();
			aiParagraphService.saveBatch(list);
			messageStreamPublisher.publish(list.stream()
				.map(item -> Message.of(MessageConstants.TOPIC_PARAGRAPH_EMBEDDING, item.getId()))
				.toList());
			aiDocumentService.changeState(docId, StateEnum.COMPLETION, StringUtils.EMPTY);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			aiDocumentService.changeState(document.getId(), StateEnum.FAILURE, e.getMessage());
		}
		return Action.CommitMessage;
	}

	@Override
	public String getGroup() {
		return "GROUP_DOCUMENT_SPLIT";
	}

	@Override
	public String getTopic() {
		return MessageConstants.TOPIC_DOCUMENT_SPLIT;
	}

}
