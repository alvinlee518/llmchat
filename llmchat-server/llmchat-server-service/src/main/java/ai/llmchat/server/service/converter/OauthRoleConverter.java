package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.vo.OauthRoleVO;
import ai.llmchat.server.repository.entity.OauthRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OauthRoleConverter {
    OauthRoleVO dto2vo(OauthRole dto);
}
