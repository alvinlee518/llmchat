package ai.llmchat.server.service.converter;

import ai.llmchat.common.langchain.enums.ModelProviderEnum;
import ai.llmchat.common.langchain.enums.ModelTypeEnum;
import ai.llmchat.server.api.vo.ModelVO;
import ai.llmchat.server.repository.entity.AiModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AiModelsConverter {

	List<ModelVO> dto2vo(Collection<AiModel> dto);

	@Mapping(source = "modelProvider", target = "modelProviderName", qualifiedByName = "toModelProviderName")
	@Mapping(source = "modelProvider", target = "modelProviderIcon", qualifiedByName = "toModelProviderIcon")
	@Mapping(source = "modelType", target = "modelTypeName", qualifiedByName = "toModelTypeName")
	ModelVO dto2vo(AiModel dto);

	@Named("toModelProviderIcon")
	public static String toModelProviderIcon(Integer modelProvider) {
		return ModelProviderEnum.valueOf(modelProvider).getIcon();
	}

	@Named("toModelProviderName")
	public static String toModelProviderName(Integer modelProvider) {
		return ModelProviderEnum.valueOf(modelProvider).getMessage();
	}

	@Named("toModelTypeName")
	public static String toModelTypeName(Integer modelType) {
		return ModelTypeEnum.valueOf(modelType).getMessage();
	}

}
