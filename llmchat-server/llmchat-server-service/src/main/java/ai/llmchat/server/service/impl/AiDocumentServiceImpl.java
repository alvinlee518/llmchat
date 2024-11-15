package ai.llmchat.server.service.impl;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.common.langchain.event.DisruptorProducer;
import ai.llmchat.server.api.enums.IndexStateEnum;
import ai.llmchat.server.api.param.DocumentPageParam;
import ai.llmchat.server.api.param.DocumentParam;
import ai.llmchat.server.api.param.FileParam;
import ai.llmchat.server.api.vo.DocumentVO;
import ai.llmchat.server.repository.dataobject.DocumentDO;
import ai.llmchat.server.repository.entity.AiDocument;
import ai.llmchat.server.repository.entity.AiParagraph;
import ai.llmchat.server.repository.mapper.AiDocumentMapper;
import ai.llmchat.server.repository.mapper.AiParagraphMapper;
import ai.llmchat.server.service.AiDocumentService;
import ai.llmchat.server.service.converter.AiDocumentConverter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    private final AiParagraphMapper aiParagraphMapper;
    private final DisruptorProducer disruptorProducer;

    public AiDocumentServiceImpl(AiDocumentConverter aiDocumentConverter, AiParagraphMapper aiParagraphMapper, DisruptorProducer disruptorProducer) {
        this.aiDocumentConverter = aiDocumentConverter;
        this.aiParagraphMapper = aiParagraphMapper;
        this.disruptorProducer = disruptorProducer;
    }

    @Override
    public PageData<DocumentVO> queryPage(DocumentPageParam param) {
        PageInfo<DocumentDO> pageInfo = PageHelper
                .startPage(param.getPage(), param.getSize())
                .doSelectPageInfo(() -> baseMapper.queryPage(param.getDatasetId(), param.getName(), param.getIndexState()));
        return PageData.of(pageInfo.getTotal(), param.getPage(), param.getSize(), aiDocumentConverter.do2vo(pageInfo.getList()));
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
        list.forEach(item -> {
            disruptorProducer.sendDocumentEvent(item.getId());
        });
    }

    @Override
    public void reindex(Long docId) {
        baseMapper.update(Wrappers.<AiDocument>lambdaUpdate()
                .eq(AiDocument::getId, docId)
                .set(AiDocument::getIndexState, IndexStateEnum.PENDING.getCode()));
        aiParagraphMapper.delete(Wrappers.<AiParagraph>lambdaQuery().eq(AiParagraph::getDocId, docId));
        disruptorProducer.sendDocumentEvent(docId);
    }
}
