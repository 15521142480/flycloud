package com.fly.auth.extend.services;

import com.fly.auth.domain.AuthClientUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * d
 *
 * @author lxs
 * @date 2023/5/3
 */
@Component
@RequiredArgsConstructor
public class UserAuthenticationConverterServices extends DefaultUserAuthenticationConverter {

    private final SysUserDetailsServices userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {

        LinkedHashMap response = new LinkedHashMap();
        String name = authentication.getName();
        response.put("username", name);

        Object principal = authentication.getPrincipal();
        AuthClientUser userJwt;
        if (principal instanceof AuthClientUser) {
            userJwt = (AuthClientUser) principal;
        } else {
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            userJwt = (AuthClientUser) userDetails;
        }
        response.put("id", userJwt.getId());
        response.put("name", userJwt.getUsername());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }

        return response;
    }
}
