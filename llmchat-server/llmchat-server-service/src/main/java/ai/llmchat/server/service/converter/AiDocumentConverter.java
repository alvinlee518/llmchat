package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.param.DocumentParam;
import ai.llmchat.server.api.vo.DocumentItemVO;
import ai.llmchat.server.api.vo.DocumentVO;
import ai.llmchat.server.repository.dataobject.DocumentItemDO;
import ai.llmchat.server.repository.entity.AiDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AiDocumentConverter {

	AiDocument param2dto(DocumentParam param);

	List<DocumentItemVO> do2vo(List<DocumentItemDO> list);

	DocumentVO dto2vo(AiDocument dto);

}
