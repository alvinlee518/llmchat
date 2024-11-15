package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.langchain.event.DisruptorProducer;
import ai.llmchat.server.api.enums.IndexStateEnum;
import ai.llmchat.server.api.param.EnabledParam;
import ai.llmchat.server.api.param.ParagraphPageParam;
import ai.llmchat.server.repository.dataobject.ParagraphDO;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.mapper.AiDocumentMapper;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import ai.llmchat.server.service.AiParagraphService;
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
    private final DisruptorProducer disruptorProducer;
    private final AiDocumentMapper aiDocumentMapper;

    public AiParagraphServiceImpl(DisruptorProducer disruptorProducer, AiDocumentMapper aiDocumentMapper) {
        this.disruptorProducer = disruptorProducer;
        this.aiDocumentMapper = aiDocumentMapper;
    }

    @Override
    public PageData<AiParagraph> queryPage(ParagraphPageParam param) {
        LambdaQueryWrapper<AiParagraph> queryWrapper = Wrappers.<AiParagraph>lambdaQuery();
        queryWrapper.eq(AiParagraph::getStatus, 1);
        queryWrapper.eq(Optional.ofNullable(param.getIndexState()).orElse(-1) >= 0, AiParagraph::getIndexState, param.getIndexState());
        queryWrapper.eq(Optional.ofNullable(param.getDocId()).orElse(0L) >= 1, AiParagraph::getDocId, param.getDocId());
        queryWrapper.and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> {
            wrapper.like(AiParagraph::getTitle, param.getKeyword()).or().like(AiParagraph::getContent, param.getKeyword());
        });
        queryWrapper.orderByAsc(AiParagraph::getPosition);
        PageInfo<AiParagraph> pageInfo = PageHelper.startPage(param.getPage(), param.getSize()).doSelectPageInfo(() -> list(queryWrapper));
        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), pageInfo.getList());
    }

    @Override
    public boolean saveOrUpdate(AiParagraph entity) {
        entity.setIndexState(IndexStateEnum.PENDING.getCode());
        entity.setFailure(StringUtils.EMPTY);
        boolean isOK = super.saveOrUpdate(entity);

        aiDocumentMapper.update(Wrappers.<AiDocument>lambdaUpdate()
                .eq(AiDocument::getId, entity.getDocId())
                .set(AiDocument::getIndexState, IndexStateEnum.IN_PROCESSING.getCode()));
        if (isOK) {
            disruptorProducer.sendDocumentEvent(entity.getDocId());
        }
        return isOK;
    }

    @Override
    public void enabled(EnabledParam param) {
        LambdaUpdateWrapper<AiParagraph> updateWrapper = Wrappers.<AiParagraph>lambdaUpdate()
                .eq(AiParagraph::getId, param.getId())
                .set(AiParagraph::getStatus, param.getEnabled());
        baseMapper.update(updateWrapper);
    }

    @Override
    public List<ParagraphDO> queryParagraphByIds(List<Long> ids) {
        return baseMapper.querySegmentByIds(ids);
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
}
