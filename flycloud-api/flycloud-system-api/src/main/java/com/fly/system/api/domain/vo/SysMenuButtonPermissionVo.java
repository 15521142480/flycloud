package com.fly.system.api.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜单按钮权限
 *
 * @author: lxs
 * @date: 2024/8/31
 */
@Data
public class SysMenuButtonPermissionVo implements Serializable {


    /**
     * 按钮名称
     */
    private String btnName;

    /**
     * 按钮权限
     */
    private String btnPermission;

    /**
     * 是否选中/角色含有该权限 （0：不选中，1：选中）
     */
//    private String flag = "0";

    /**
     * 是否选中/角色含有该权限 （新版）
     */
    private Boolean checked = false;


}
