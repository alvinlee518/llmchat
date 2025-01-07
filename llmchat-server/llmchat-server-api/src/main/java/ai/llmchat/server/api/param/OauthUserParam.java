package ai.llmchat.server.api.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OauthUserParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Long id;

	/**
	 * 部门Id
	 */
	private Long deptId;

	/**
	 * 岗位Id列表
	 */
	private List<Long> postIds;

	/**
	 * 角色Id列表
	 */
	private List<Long> roleIds;

	/**
	 * 姓名
	 */
	@NotBlank
	private String name;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 性别:0-未知;1-男;2-女;
	 */
	private Integer gender;

	/**
	 * 生日
	 */
	private LocalDate birthday;

	/**
	 * 备注
	 */
	private String remark;

}
