package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

import lombok.experimental.Accessors;

/**
 * 角色权限对象 sys_role_menu
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@Accessors(chain = true)
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 菜单id
     */
    private Long menuId;
    /**
     * 权限
     */
    private String permission;

}
