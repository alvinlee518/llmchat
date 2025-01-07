package ai.llmchat.server.api.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DataScopeParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 数据范围(0-全部数据权限;1-自定义数据权限-;2-本部门数据权限;3-本部门及以下数据权限;4-仅本人数据权限)
	 */
	private Integer dataScope;

	/**
	 * 部门Id
	 */
	private List<Long> deptIds;

}
