package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.enums.BooleanEnum;
import ai.llmchat.common.core.exception.ServiceException;
import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.redis.core.Message;
import ai.llmchat.common.redis.core.MessageStreamPublisher;
import ai.llmchat.server.api.enums.StateEnum;
import ai.llmchat.server.api.param.EnabledParam;
import ai.llmchat.server.api.param.ParagraphPageParam;
import ai.llmchat.server.api.vo.ParagraphExportVO;
import ai.llmchat.server.repository.dataobject.ParagraphDO;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import ai.llmchat.server.service.AiParagraphService;
import ai.llmchat.server.service.consumer.MessageConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 数据文档 服务实现类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
@Service
public class AiParagraphServiceImpl extends ServiceImpl<AiParagraphMapper, AiParagraph> implements AiParagraphService {

	private final MessageStreamPublisher messageStreamPublisher;

	public AiParagraphServiceImpl(MessageStreamPublisher messageStreamPublisher) {
		this.messageStreamPublisher = messageStreamPublisher;
	}

	@Override
	public PageData<AiParagraph> queryPage(ParagraphPageParam param) {
		LambdaQueryWrapper<AiParagraph> queryWrapper = Wrappers.<AiParagraph>lambdaQuery();
		queryWrapper.eq(AiParagraph::getStatus, 1);
		queryWrapper.eq(Optional.ofNullable(param.getState()).orElse(-1) >= 0, AiParagraph::getState, param.getState());
		queryWrapper.eq(Optional.ofNullable(param.getDocId()).orElse(0L) >= 1, AiParagraph::getDocId, param.getDocId());
		queryWrapper.and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> {
			wrapper.like(AiParagraph::getTitle, param.getKeyword())
				.or()
				.like(AiParagraph::getContent, param.getKeyword());
		});
		queryWrapper.orderByAsc(AiParagraph::getPosition);
		PageInfo<AiParagraph> pageInfo = PageHelper.startPage(param.getPage(), param.getSize())
			.doSelectPageInfo(() -> list(queryWrapper));
		return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
	}

	@Override
	public boolean saveOrUpdate(AiParagraph entity) {
		entity.setState(StateEnum.PENDING.getCode());
		entity.setFailure(StringUtils.EMPTY);
		boolean isOk = super.saveOrUpdate(entity);
		if (isOk) {
			messageStreamPublisher.publish(Message.of(MessageConstants.TOPIC_PARAGRAPH_EMBEDDING, entity.getId()));
		}
		return isOk;
	}

	@Override
	public void enabled(EnabledParam param) {
		AiParagraph paragraph = baseMapper.selectById(param.getId());
		if (Optional.ofNullable(paragraph).map(AiParagraph::getId).orElse(0L) <= 0) {
			throw new ServiceException("分段不存在");
		}
		paragraph.setStatus(param.getEnabled());
		baseMapper.updateById(paragraph);
		if (Objects.equals(BooleanEnum.YES.getCode(), param.getEnabled())) {
			messageStreamPublisher.publish(Message.of(MessageConstants.TOPIC_PARAGRAPH_EMBEDDING, paragraph.getId()));
		}
	}

	@Override
	public List<ParagraphDO> queryParagraphByIds(List<Long> ids) {
		return baseMapper.queryByIds(ids);
	}

	@Override
	public void incrementHitCount(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		LambdaUpdateWrapper<AiParagraph> updateWrapper = Wrappers.<AiParagraph>lambdaUpdate()
			.setIncrBy(AiParagraph::getHitCount, 1)
			.in(AiParagraph::getId, ids);
		baseMapper.update(updateWrapper);
	}

	@Override
	public void removeByDocId(Long docId) {
		LambdaQueryWrapper<AiParagraph> queryWrapper = Wrappers.<AiParagraph>lambdaQuery()
			.eq(AiParagraph::getDocId, docId);
		baseMapper.delete(queryWrapper);
	}

	@Override
	public List<AiParagraph> listPendingByDocId(Long docId) {
		LambdaQueryWrapper<AiParagraph> queryWrapper = Wrappers.<AiParagraph>lambdaQuery()
			.eq(AiParagraph::getDocId, docId)
			.eq(AiParagraph::getState, StateEnum.PENDING.getCode())
			.eq(AiParagraph::getStatus, BooleanEnum.YES.getCode());
		return baseMapper.selectList(queryWrapper);
	}

	@Override
	public void changeState(List<Long> ids, StateEnum state) {
		LambdaUpdateWrapper<AiParagraph> updateWrapper = Wrappers.<AiParagraph>lambdaUpdate()
			.set(AiParagraph::getState, state.getCode())
			.in(AiParagraph::getId, ids);
		baseMapper.update(updateWrapper);
	}

	@Override
	public void changeState(List<Long> ids, StateEnum state, String failure) {
		LambdaUpdateWrapper<AiParagraph> updateWrapper = Wrappers.<AiParagraph>lambdaUpdate()
			.set(AiParagraph::getState, state.getCode())
			.set(AiParagraph::getFailure, failure)
			.in(AiParagraph::getId, ids);
		baseMapper.update(updateWrapper);
	}

	@Override
	public void reindexByDocId(Long docId) {
		List<AiParagraph> list = baseMapper.selectList(Wrappers.<AiParagraph>lambdaQuery()
			.eq(AiParagraph::getDocId, docId)
			.eq(AiParagraph::getStatus, BooleanEnum.YES.getCode())
			.select(AiParagraph::getId));
		if (CollectionUtils.isNotEmpty(list)) {
			List<Long> idList = list.stream().map(AiParagraph::getId).toList();
			baseMapper.update(Wrappers.<AiParagraph>lambdaUpdate()
				.set(AiParagraph::getState, StateEnum.PENDING.getCode())
				.set(AiParagraph::getFailure, StringUtils.EMPTY)
				.eq(AiParagraph::getDocId, docId)
				.in(AiParagraph::getId, idList));
			List<Message> messages = list.stream()
				.map(item -> Message.of(MessageConstants.TOPIC_PARAGRAPH_EMBEDDING, item.getId()))
				.toList();
			messageStreamPublisher.publish(messages);
		}
	}

	@Override
	public void reindexByDatasetId(Long dsId) {
		List<AiParagraph> list = baseMapper.selectList(Wrappers.<AiParagraph>lambdaQuery()
			.eq(AiParagraph::getDatasetId, dsId)
			.eq(AiParagraph::getStatus, BooleanEnum.YES.getCode())
			.select(AiParagraph::getId));
		if (CollectionUtils.isNotEmpty(list)) {
			List<Long> idList = list.stream().map(AiParagraph::getId).toList();
			baseMapper.update(Wrappers.<AiParagraph>lambdaUpdate()
				.set(AiParagraph::getState, StateEnum.PENDING.getCode())
				.set(AiParagraph::getFailure, StringUtils.EMPTY)
				.eq(AiParagraph::getDatasetId, dsId)
				.in(AiParagraph::getId, idList));
			List<Message> messages = list.stream()
				.map(item -> Message.of(MessageConstants.TOPIC_PARAGRAPH_EMBEDDING, item.getId()))
				.toList();
			messageStreamPublisher.publish(messages);
		}
	}

	@Override
	public void reindex(Long paraId) {
		AiParagraph paragraph = baseMapper.selectById(paraId);
		if (Optional.ofNullable(paragraph).map(AiParagraph::getId).orElse(0L) <= 0) {
			throw new ServiceException("分段不存在");
		}
		paragraph.setState(StateEnum.PENDING.getCode());
		paragraph.setFailure(StringUtils.EMPTY);
		baseMapper.updateById(paragraph);
		messageStreamPublisher.publish(Message.of(MessageConstants.TOPIC_PARAGRAPH_EMBEDDING, paragraph.getId()));
	}

	@Override
	public List<ParagraphExportVO> exportListByDocId(Long docId) {
		LambdaQueryWrapper<AiParagraph> queryWrapper = Wrappers.<AiParagraph>lambdaQuery()
			.eq(AiParagraph::getDocId, docId)
			.eq(AiParagraph::getStatus, BooleanEnum.YES.getCode())
			.select(AiParagraph::getTitle, AiParagraph::getContent);
		List<AiParagraph> list = baseMapper.selectList(queryWrapper);
		return list.stream().map(item -> {
			ParagraphExportVO exportVO = new ParagraphExportVO();
			exportVO.setTitle(item.getTitle());
			exportVO.setContent(item.getContent());
			return exportVO;
		}).toList();
	}

}
