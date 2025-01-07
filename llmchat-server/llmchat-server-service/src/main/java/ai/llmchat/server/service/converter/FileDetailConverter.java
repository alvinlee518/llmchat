package ai.llmchat.server.service.converter;

import ai.llmchat.server.repository.entity.FileDetail;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.hash.HashInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface FileDetailConverter {

	@Mappings({ @Mapping(source = "metadata", target = "metadata", qualifiedByName = "toJson"),
			@Mapping(source = "userMetadata", target = "userMetadata", qualifiedByName = "toJson"),
			@Mapping(source = "thMetadata", target = "thMetadata", qualifiedByName = "toJson"),
			@Mapping(source = "thUserMetadata", target = "thUserMetadata", qualifiedByName = "toJson"),
			@Mapping(source = "attr", target = "attr", qualifiedByName = "toJson"),
			@Mapping(source = "hashInfo", target = "hashInfo", qualifiedByName = "toJson"),
			@Mapping(target = "fileAcl", ignore = true), @Mapping(target = "thFileAcl", ignore = true), })
	FileDetail dto2do(FileInfo info);

	@Mappings({ @Mapping(source = "metadata", target = "metadata", qualifiedByName = "toMap"),
			@Mapping(source = "userMetadata", target = "userMetadata", qualifiedByName = "toMap"),
			@Mapping(source = "thMetadata", target = "thMetadata", qualifiedByName = "toMap"),
			@Mapping(source = "thUserMetadata", target = "thUserMetadata", qualifiedByName = "toMap"),
			@Mapping(source = "attr", target = "attr", qualifiedByName = "toDict"),
			@Mapping(source = "hashInfo", target = "hashInfo", qualifiedByName = "toHashInfo"),
			@Mapping(target = "fileAcl", ignore = true), @Mapping(target = "thFileAcl", ignore = true), })
	FileInfo do2dto(FileDetail detail);

	@Named("toJson")
	public static String toJson(Object info) {
		if (Objects.isNull(info)) {
			return StringUtils.EMPTY;
		}
		return JSON.toJSONString(info);
	}

	@Named("toMap")
	public static Map<String, String> toMap(String json) {
		if (StringUtils.isEmpty(json)) {
			return new HashMap<>();
		}
		return JSON.parseObject(json, new TypeReference<Map<String, String>>() {
		});
	}

	@Named("toDict")
	public static Dict toDict(String json) {
		if (StringUtils.isEmpty(json)) {
			return new Dict();
		}
		return JSON.parseObject(json, Dict.class);
	}

	@Named("toHashInfo")
	public static HashInfo toHashInfo(String json) {
		if (StringUtils.isEmpty(json)) {
			return new HashInfo();
		}
		return JSON.parseObject(json, HashInfo.class);
	}

}
