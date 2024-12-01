package com.fly.common.security.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 自定义授权用户
 *
 * <p>
 * 需继承spring security的User；用于自身密码的自动判断和角色权限判断，拓展的字段用于业务实现
 *
 * @author: lxs
 * @date: 2024/8/12
 */
@Getter
public class FlyUser extends User {


    private static final long serialVersionUID = -5768257947433986L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 部门ID
     */
    private final String deptId;

    /**
     * 手机号
     */
    private final String phone;

    /**
     * 头像
     */
    private final String avatar;

    /**
     * 用户类型
     */
    private final int userType;

    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
     */
    private final int loginType;

    /**
     * 系统权限标识组
     */
    private final String roleIds;


    /**
     * 构造系统用户信息
     */
    public FlyUser(Long id, int userType, int loginType, String deptId, String phone, String avatar, String roleIds, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.userType = userType;
        this.loginType = loginType;
        this.deptId = deptId;
        this.phone = phone;
        this.avatar = avatar;
        this.roleIds = roleIds;
    }
}
