package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.enums.StateEnum;
import ai.llmchat.server.api.param.DocumentPageParam;
import ai.llmchat.server.api.param.DocumentParam;
import ai.llmchat.server.api.param.FileParam;
import ai.llmchat.server.api.vo.DocumentItemVO;
import ai.llmchat.server.repository.entity.AiDocument;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据文档 服务类
 * </p>
 *
 * @author lixw
 * @since 2024-10-28
 */
public interface AiDocumentService extends IService<AiDocument> {
    PageData<DocumentItemVO> queryPage(DocumentPageParam param);

    void saveOrUpdate(DocumentParam param, List<FileParam> fileParamList);

    void saveOrUpdate(DocumentParam param);

    void changeState(Long docId, StateEnum state, String failure);
}
