package ai.llmchat.server.api.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * 角色
 *
 * @TableName oauth_role
 */
@Data
public class OauthRoleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long id;

    /**
     * 部门Id
     */
    private List<Long> deptIds;

    /**
     * 菜单Id
     */
    private List<Long> menuIds;

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
     * 排序
     */
    private Integer sorting;

    /**
     * 备注
     */
    private String remark;
}