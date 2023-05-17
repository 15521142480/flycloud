package com.fly.auth.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 认证中心客户端用户
 *
 * @author lxs
 * @date 2023/5/3
 */
@Getter
public class AuthClientUser extends User {

//    /**
//     * 客户端标识 ID
//     */
//    private String clientId;
//
//    /**
//     * 客户端安全码
//     */
//    private String clientSecret;


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

    public AuthClientUser(Long id, int type, Long departId, String roleId, String phone, String avatar, String tenantId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
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
