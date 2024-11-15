package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.param.DocumentParam;
import ai.llmchat.server.api.vo.DocumentVO;
import ai.llmchat.server.repository.dataobject.DocumentDO;
import ai.llmchat.server.repository.entity.AiDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AiDocumentConverter {
    AiDocument param2dto(DocumentParam param);

    List<DocumentVO> do2vo(List<DocumentDO> list);
}
