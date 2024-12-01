package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * 菜单对象 sys_menu
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 菜单ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 菜单类型（sys_type）
     */
    private Long type;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 菜单权限
     */
    private String permission;
    /**
     * 按钮权限
     */
    private String buttonPermission;
    /**
     * 排序值
     */
    private Long sort;
    /**
     * 菜单等级
     */
    private Long level;
    /**
     * 路由路径
     */
    private String path;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 组件名
     */
    private String componentName;
    /**
     * 状态（0:启用，1:禁用）
     */
    private Integer status;
    /**
     * 是否可见
     */
    private Integer visible;
    /**
     * 是否缓存该页面: 1:是  0:不是
     */
    private Integer keepAlive;
    /**
     * 是否总是显示
     */
    private Integer alwaysShow;
    /**
     * 是否外链
     */
    private Integer target;
    /**
     * 备注
     */
    private String remark;
}
