package com.fly.common.security.component;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 权限判断
 *
 * @author: lxs
 * @date: 2024/8/24
 */
@Component(value = "pms")
public class PermissionService {


    /**
     * 判断接口是否有权限
     *
     * <p>
     * 格式为xxx:xxx或xxx:xxx:xxx，如 @PreAuthorize("@pms.hasPermission('sys:user:add')")
     *
     */
    public boolean hasPermission(String permission) {

        if (StrUtil.isBlank(permission)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
    }

}