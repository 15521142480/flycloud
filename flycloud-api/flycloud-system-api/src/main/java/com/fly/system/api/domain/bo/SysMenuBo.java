package com.fly.system.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * 菜单业务对象
 *
 * @author fly
 * @date 2024-11-24
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
     * 菜单名称
     */
    // @NotBlank(message = "菜单名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 父菜单ID
     */
    // @NotNull(message = "父菜单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 菜单权限
     */
    // @NotBlank(message = "菜单权限不能为空", groups = { AddGroup.class, EditGroup.class })
    private String permission;

    /**
     * 按钮权限
     */
    // @NotBlank(message = "按钮权限不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buttonPermission;

    /**
     * 排序值
     */
    // @NotNull(message = "排序值不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 菜单等级
     */
    // @NotNull(message = "菜单等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long level;

    /**
     * 路由路径
     */
    // @NotBlank(message = "路由路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String path;

    /**
     * 菜单图标
     */
    // @NotBlank(message = "菜单图标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String icon;

    /**
     * 组件路径
     */
    // @NotBlank(message = "组件路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String component;

    /**
     * 组件名
     */
    // @NotBlank(message = "组件名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String componentName;

    /**
     * 状态（0:启用，1:禁用）
     */
    // @NotBlank(message = "状态（0:启用，1:禁用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 是否可见
     */
    // @NotNull(message = "是否可见不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer visible;

    /**
     * 是否缓存该页面: 1:是  0:不是
     */
    // @NotNull(message = "是否缓存该页面: 1:是  0:不是不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer keepAlive;

    /**
     * 是否总是显示
     */
    // @NotNull(message = "是否总是显示不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer alwaysShow;

    /**
     * 是否外链
     */
    // @NotNull(message = "是否外链不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer target;

    /**
     * 备注
     */
    // @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

}
