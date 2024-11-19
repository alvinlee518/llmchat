package ai.llmchat.server.service;

import ai.llmchat.common.core.wrapper.data.PageData;
import ai.llmchat.server.api.enums.StateEnum;
import ai.llmchat.server.api.param.EnabledParam;
import ai.llmchat.server.api.param.ParagraphPageParam;
import ai.llmchat.server.api.vo.ParagraphExportVO;
import ai.llmchat.server.repository.dataobject.ParagraphDO;
import ai.llmchat.server.repository.entity.AiParagraph;
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
public interface AiParagraphService extends IService<AiParagraph> {
    PageData<AiParagraph> queryPage(ParagraphPageParam param);

    void enabled(EnabledParam param);

    List<ParagraphDO> queryParagraphByIds(List<Long> ids);

    void incrementHitCount(List<Long> ids);

    void removeByDocId(Long docId);

    List<AiParagraph> listPendingByDocId(Long docId);

    void changeState(List<Long> ids, StateEnum state);

    void changeState(List<Long> ids, StateEnum state, String failure);

    void reindexByDocId(Long docId);

    void reindexByDatasetId(Long dsId);

    void reindex(Long paraId);

    List<ParagraphExportVO> exportListByDocId(Long docId);
}
