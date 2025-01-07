package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.param.DatasetParam;
import ai.llmchat.server.api.vo.DatasetVO;
import ai.llmchat.server.repository.dataobject.DatasetDO;
import ai.llmchat.server.repository.entity.AiDataset;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AiDatasetConverter {

	AiDataset param2dto(DatasetParam param);

	DatasetVO do2vo(DatasetDO param);

	List<DatasetVO> do2vo(List<DatasetDO> list);

}
