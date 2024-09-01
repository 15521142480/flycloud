package com.fly.system.api.domain.bo;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.fly.common.database.web.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * 菜单业务对象
 *
 * @author fly
 * @date 2024-08-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysMenuBo extends BaseEntity {

    /**
     * 菜单ID
     */
    // @NotNull(message = "菜单ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 菜单类型（sys_type）
     */
    // @NotNull(message = "菜单类型（sys_type）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 菜单标题
     */
    // @NotBlank(message = "菜单标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 父菜单ID
     */
    // @NotNull(message = "父菜单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 菜单
     */
    // @NotBlank(message = "菜单不能为空", groups = { AddGroup.class, EditGroup.class })
    private String permission;

    /**
     * 按钮权限
     */
    // @NotBlank(message = "按钮权限不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buttonPermission;

    /**
     * 菜单等级
     */
    private Long level;

    /**
     * 路由路径
     */
    // @NotBlank(message = "路由路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String path;

    /**
     * 组件路径
     */
    // @NotBlank(message = "组件路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String component;

    /**
     * 菜单图标
     */
    // @NotBlank(message = "菜单图标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String icon;

    /**
     * 状态（0:启用，1:禁用）
     */
    // @NotBlank(message = "状态（0:启用，1:禁用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 排序值
     */
    // @NotNull(message = "排序值不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 备注
     */
    // @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 是否缓存该页面: 1:是  0:不是
     */
    // @NotNull(message = "是否缓存该页面: 1:是  0:不是不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer keepAlive;

    /**
     * 是否隐藏
     */
    // @NotNull(message = "是否隐藏不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer hidden;

    /**
     * 是否外链
     */
    // @NotNull(message = "是否外链不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer target;

    /**
     * 删除标识
     */
    // @NotNull(message = "删除标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isDeleted;


}
