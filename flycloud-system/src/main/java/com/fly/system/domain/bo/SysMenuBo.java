package com.fly.system.domain.bo;

import com.fly.common.database.web.domain.BaseEntity;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单权限业务对象
 *
 * @author fly
 * @date 2023-04-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuBo extends BaseEntity {

    /**
     * 菜单ID
     */
    @NotNull(message = "菜单ID不能为空", groups = { EditGroup.class })
    private Long menuId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String menuName;

    /**
     * 父菜单ID
     */
    @NotNull(message = "父菜单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderNum;

    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String path;

    /**
     * 组件路径
     */
    @NotBlank(message = "组件路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String component;

    /**
     * 路由参数
     */
    @NotBlank(message = "路由参数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String queryParam;

    /**
     * 是否为外链（0是 1否）
     */
    @NotNull(message = "是否为外链（0是 1否）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @NotNull(message = "是否缓存（0缓存 1不缓存）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @NotBlank(message = "菜单类型（M目录 C菜单 F按钮）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @NotBlank(message = "显示状态（0显示 1隐藏）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @NotBlank(message = "菜单状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 权限标识
     */
    @NotBlank(message = "权限标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String perms;

    /**
     * 菜单图标
     */
    @NotBlank(message = "菜单图标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String icon;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
