package ai.llmchat.server.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CitationVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String url;

	private List<SegmentVO> segments;

}