package com.fly.auth.controller;

import com.fly.auth.config.properties.SecurityAuthorizationProperties;
import com.fly.auth.domain.AuthClientUser;
import com.fly.auth.domain.bo.SysUserLoginBo;
import com.fly.common.admain.bo.AuthLoginBo;
import com.fly.common.model.R;
import com.fly.common.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

/**
 * 认证中心-控制层
 *
 * @author lxs
 * @date 2023/5/2
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final SecurityAuthorizationProperties securityAuthorizationProperties;

    private final TokenEndpoint tokenEndpoint;


    /**
     * 登录
     *
    */
    @PostMapping("/login")
    public R<OAuth2AccessToken> login(Principal principal, @RequestBody SysUserLoginBo sysUserLoginBo) throws HttpRequestMethodNotSupportedException {

//        Object principal1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User clientUser = new User(securityAuthorizationProperties.getClientId(), securityAuthorizationProperties.getClientSecret(), new ArrayList<>());
//        AuthClientUser clientUser = new AuthClientUser(securityAuthorizationProperties.getClientId(), securityAuthorizationProperties.getClientSecret(), new ArrayList<>());
        UsernamePasswordAuthenticationToken principal2 = new UsernamePasswordAuthenticationToken(clientUser,null, new ArrayList<>());

        // 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
        //  方式一：client_id、client_secret放在请求路径中(注：当前版本已废弃)
        //  方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret

        AuthLoginBo authLoginBo = new AuthLoginBo();
        authLoginBo.setClient_id(securityAuthorizationProperties.getClientId());
        authLoginBo.setClient_secret(securityAuthorizationProperties.getClientSecret());
        authLoginBo.setGrant_type(securityAuthorizationProperties.getGrantTypes()[0]);
        authLoginBo.setUsername(sysUserLoginBo.getUsername());
        authLoginBo.setPassword(sysUserLoginBo.getPassword());
        log.info("OAuth认证授权 -> 客户端ID: {}, 请求参数: {}", authLoginBo.getClient_id(), JsonUtils.toJsonString(authLoginBo));

        // todo 接口文档测试使用
//        if (SecurityConstants.TEST_CLIENT_ID.equals(clientId)) {
//            return tokenEndpoint.postAccessToken(principal, parameters).getBody();
//        }

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal2, JsonUtils.parseMap(authLoginBo)).getBody();

        return R.ok(accessToken);
    }


}
