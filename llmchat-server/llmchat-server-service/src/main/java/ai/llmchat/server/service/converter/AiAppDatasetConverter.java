package ai.llmchat.server.service.converter;

import ai.llmchat.server.api.vo.AppDatasetVO;
import ai.llmchat.server.api.vo.DatasetItemVO;
import ai.llmchat.server.repository.dataobject.AppDatasetDO;
import ai.llmchat.server.repository.dataobject.DatasetItemDO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AiAppDatasetConverter {
    List<AppDatasetVO> do2vo(List<AppDatasetDO> list);

    List<DatasetItemVO> itemDo2vo(List<DatasetItemDO> list);
}
