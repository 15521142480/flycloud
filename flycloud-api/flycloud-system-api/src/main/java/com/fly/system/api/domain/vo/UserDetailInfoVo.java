package com.fly.system.api.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户详情信息 - vo
 *
 * @author: lxs
 * @date: 2024/9/2
 */
@Data
public class UserDetailInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private SysUserVo user;

    /**
     * 系统权限标识组
     */
    private List<String> permissionList;

    /**
     * 系统菜单树列表
     */
    private List<SysMenuTreeVo> menuTreeList;

}
