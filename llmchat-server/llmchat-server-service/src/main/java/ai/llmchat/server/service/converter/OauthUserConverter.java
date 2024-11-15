package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.param.OauthUserParam;
import ai.llmchat.server.api.vo.OauthUserVO;
import ai.llmchat.server.repository.entity.OauthUser;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OauthUserConverter {
    OauthUser param2dto(OauthUserParam param);

    OauthUserVO dto2vo(OauthUser dto);

    List<OauthUserVO> dto2vo(Collection<OauthUser> list);
}
