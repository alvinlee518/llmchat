package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.redis.core.Message;
import ai.llmchat.common.redis.core.MessageStreamPublisher;
import ai.llmchat.server.api.enums.StateEnum;
import ai.llmchat.server.api.param.DocumentPageParam;
import ai.llmchat.server.api.param.DocumentParam;
import ai.llmchat.server.api.param.FileParam;
import ai.llmchat.server.api.vo.DocumentItemVO;
import ai.llmchat.server.repository.dataobject.DocumentItemDO;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.mapper.AiDocumentMapper;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import ai.llmchat.server.service.AiDocumentService;
import ai.llmchat.server.service.consumer.MessageConstants;
import ai.llmchat.server.service.converter.AiDocumentConverter;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据文档 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Service
public class AiDocumentServiceImpl extends ServiceImpl<AiDocumentMapper, AiDocument> implements AiDocumentService {

	private final AiDocumentConverter aiDocumentConverter;

	private final MessageStreamPublisher messageStreamPublisher;

	public AiDocumentServiceImpl(AiDocumentConverter aiDocumentConverter,
			MessageStreamPublisher messageStreamPublisher) {
		this.aiDocumentConverter = aiDocumentConverter;
		this.messageStreamPublisher = messageStreamPublisher;
	}

	@Override
	public PageData<DocumentItemVO> queryPage(DocumentPageParam param) {
		PageInfo<DocumentItemDO> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> baseMapper.queryPage(param.getDatasetId(), param.getName(), param.getState()));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(),
				aiDocumentConverter.do2vo(pageInfo.getList()));
	}

	@Override
	public void saveOrUpdate(DocumentParam param, List<FileParam> fileParamList) {
		List<AiDocument> list = fileParamList.stream().map(file -> {
			AiDocument document = aiDocumentConverter.param2dto(param);
			document.setFileId(file.getFileId());
			document.setName(file.getFileName());
			return document;
		}).toList();
		super.saveBatch(list);
		List<Message> messageList = list.stream()
			.map(item -> new Message(MessageConstants.TOPIC_DOCUMENT_SPLIT, String.valueOf(item.getId())))
			.toList();
		messageStreamPublisher.publish(messageList);
	}

	@Override
	public void saveOrUpdate(DocumentParam param) {
		AiDocument document = new AiDocument();
		document.setId(param.getId());
		document.setSegmentMode(param.getSegmentMode());
		document.setSeparators(param.getSeparators());
		document.setChunkSize(param.getChunkSize());
		document.setChunkOverlap(param.getChunkOverlap());
		document.setCleanRules(param.getCleanRules());
		document.setState(StateEnum.PENDING.getCode());
		document.setFailure(StringUtils.EMPTY);
		document.setEmbedCols(param.getEmbedCols());
		document.setReplyCols(param.getReplyCols());
		document.setStatus(BooleanEnum.YES.getCode());
		baseMapper.updateById(document);
		messageStreamPublisher.publish(Message.of(MessageConstants.TOPIC_DOCUMENT_SPLIT, document.getId()));
	}

	@Override
	public void changeState(Long docId, StateEnum state, String failure) {
		LambdaUpdateWrapper<AiDocument> updateWrapper = Wrappers.<AiDocument>lambdaUpdate()
			.eq(AiDocument::getId, docId)
			.set(AiDocument::getState, state.getCode())
			.set(AiDocument::getFailure, failure);
		baseMapper.update(updateWrapper);
	}

}
