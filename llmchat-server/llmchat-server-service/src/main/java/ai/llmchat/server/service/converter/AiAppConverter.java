package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.param.AppParam;
import ai.llmchat.server.api.vo.AppVO;
import ai.llmchat.server.repository.entity.AiApp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AiAppConverter {

	AiApp param2do(AppParam param);

	AppVO do2vo(AiApp app);

	List<AppVO> do2vo(List<AiApp> list);

}
