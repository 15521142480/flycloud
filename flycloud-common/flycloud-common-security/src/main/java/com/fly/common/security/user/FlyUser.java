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
    private final Long id;

    /**
     * 部门ID
     */
    private final String roleId;
    /**
     * 部门ID
     */
    private final Long departId;

    /**
     * 手机号
     */
    private final String phone;

    /**
     * 头像
     */
    private final String avatar;

    /**
     * 租户ID
     */
    private final String tenantId;

    /**
     * 登录类型
     */
    private final int type;

    public FlyUser(Long id, int type, Long departId, String roleId, String phone, String avatar, String tenantId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.type = type;
        this.roleId = roleId;
        this.departId = departId;
        this.phone = phone;
        this.avatar = avatar;
        this.tenantId = tenantId;
    }
}
