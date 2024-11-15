package ai.llmchat.server.repository.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author lixw
 * @since 2024-10-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("oauth_menu")
public class OauthMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 上级Id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 类型:0-目录;1-菜单;2-按钮
     */
    @TableField("menu_type")
    private Integer menuType;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 请求地址
     */
    @TableField("path")
    private String path;

    /**
     * 组件地址
     */
    @TableField("component")
    private String component;

    /**
     * 权限标识
     */
    @TableField("perms")
    private String perms;

    /**
     * 打开方式:0-页签;2-新窗口
     */
    @TableField("target")
    private Integer target;

    /**
     * 隐藏路由:0-否;2-是
     */
    @TableField("hidden")
    private Integer hidden;

    /**
     * 缓存网页:0-否;2-是
     */
    @TableField("keep_alive")
    private Integer keepAlive;

    /**
     * 排序
     */
    @TableField("sorting")
    private Integer sorting;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态:0-无效;1-有效
     */
    @TableField(value = "status", fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_at", fill = FieldFill.INSERT)
    private LocalDateTime createAt;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;
}
