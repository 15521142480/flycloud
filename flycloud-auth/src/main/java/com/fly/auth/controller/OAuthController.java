package com.fly.auth.controller;

import com.fly.common.domain.model.R;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 授权中心-控制层
 *
 * @author lxs
 * @date 2023/5/2
 */
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final TokenEndpoint tokenEndpoint;

    @Qualifier("consumerTokenServices")
    private final ConsumerTokenServices consumerTokenServices;



    /**
     * 登录
     *
     * <p>
     * 除普通参数之外，header还需传Authorization，也就是oauth的客户端，格式是：Basic 客户端key:客户端secret，如 Basic Zmx5OmZseV9zZWNyZXQ=
     */
    @GetMapping("/token")
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
     * <p>
     * 除普通参数之外，header还需传Authorization，也就是oauth的客户端，格式是：Basic 客户端key:客户端secret，如 Basic Zmx5OmZseV9zZWNyZXQ=
    */
    @PostMapping("/token")
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


    /**
     * 登出
     */
    @PostMapping("/logOut")
    @Operation(summary = "登出", description = "登出")
    @Parameters({
            @Parameter(name = "Authorization", required = true,  description = "授权码", in = ParameterIn.QUERY)
    })
    public R<?> logout(HttpServletRequest request) {

        if (StringUtils.isNotBlank(SecurityUtils.getHeaderToken(request))) {
            consumerTokenServices.revokeToken(SecurityUtils.getToken(request));
        }

        return R.ok(null, "登出成功");
    }

}
