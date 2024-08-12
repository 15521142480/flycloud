package com.fly.auth.controller;

import com.fly.auth.config.properties.SecurityAuthorizationProperties;
import com.fly.common.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
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


    private final TokenEndpoint tokenEndpoint;

    private final SecurityAuthorizationProperties securityAuthorizationProperties;


    /**
     * 登录
     *
     */
    @GetMapping("/login")
    public R<Map<String, Object>> getAccessToken(Principal principal, @RequestParam Map<String, String> loginParam) throws HttpRequestMethodNotSupportedException {

        OAuth2AccessToken accessToken =  tokenEndpoint.postAccessToken(principal, loginParam).getBody();

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> resultData = new LinkedHashMap<>(token.getAdditionalInformation());
        resultData.put("accessToken", token.getValue());
        if (token.getRefreshToken() != null) {
            resultData.put("refreshToken", token.getRefreshToken().getValue());
        }

        return R.ok(resultData);
    }

    /**
     * 登录
     *
    */
    @PostMapping("/login")
    @Parameters({
            @Parameter(name = "grant_type", required = true,  description = "授权类型", in = ParameterIn.QUERY),
            @Parameter(name = "username", required = true,  description = "用户名", in = ParameterIn.QUERY),
            @Parameter(name = "password", required = true,  description = "密码", in = ParameterIn.QUERY),
            @Parameter(name = "scope", required = true,  description = "使用范围", in = ParameterIn.QUERY),
    })
    public R<Map<String, Object>> postAccessToken(Principal principal, @RequestBody Map<String, String> loginParam) throws HttpRequestMethodNotSupportedException { // FlyUserLoginBo flyUserLoginBo

        OAuth2AccessToken accessToken =  tokenEndpoint.postAccessToken(principal, loginParam).getBody();

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> resultData = new LinkedHashMap<>(token.getAdditionalInformation());
        resultData.put("accessToken", token.getValue());
        if (token.getRefreshToken() != null) {
            resultData.put("refreshToken", token.getRefreshToken().getValue());
        }

        return R.ok(resultData);
    }


}
