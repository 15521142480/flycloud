package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.fly.common.database.web.domain.BaseEntity;

/**
 * 菜单对象 sys_menu
 *
 * @author fly
 * @date 2024-08-31
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
     * 菜单标题
     */
    private String name;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 菜单
     */
    private String permission;
    /**
     * 按钮权限
     */
    private String buttonPermission;
    /**
     * 菜单等级
     */
    private Long level;
    /**
     * 路由路径
     */
    private String path;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 状态（0:启用，1:禁用）
     */
    private String status;
    /**
     * 排序值
     */
    private Long sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否缓存该页面: 1:是  0:不是
     */
    private Integer keepAlive;
    /**
     * 是否隐藏
     */
    private Integer hidden;
    /**
     * 是否外链
     */
    private Integer target;
    /**
     * 删除标识
     */
    private Integer isDeleted;

}
