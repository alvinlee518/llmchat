package ai.llmchat.server.service.converter;

import ai.llmchat.common.langchain.rag.output.Citation;
import ai.llmchat.common.langchain.rag.output.Segment;
import ai.llmchat.server.api.param.ChatParam;
import ai.llmchat.server.api.vo.ChatVO;
import ai.llmchat.server.api.vo.CitationVO;
import ai.llmchat.server.api.vo.SegmentVO;
import ai.llmchat.server.repository.dataobject.CitationDO;
import ai.llmchat.server.repository.dataobject.SegmentDO;
import ai.llmchat.server.repository.entity.AiChat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AiChatConverter {
    List<ChatVO> dto2vo(Collection<AiChat> list);

    @Mappings({@Mapping(source = "id", target = "chatId"),})
    ChatVO dto2vo(AiChat dto);

    List<CitationDO> citationDto2do(Collection<Citation> list);

    List<CitationVO> citationDo2vo(Collection<CitationDO> list);

    List<SegmentDO> segmentDto2Do(Collection<Segment> list);

    List<SegmentVO> segmentDo2vo(Collection<SegmentDO> list);

    @Mappings({@Mapping(source = "chatId", target = "id"),})
    AiChat param2dto(ChatParam param);
}
