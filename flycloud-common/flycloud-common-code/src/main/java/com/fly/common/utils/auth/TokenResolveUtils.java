package com.fly.common.utils.auth;

import cn.hutool.core.util.StrUtil;
import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.Oauth2Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;

/**
 * Token 解析工具类，统一支持请求头和 query 参数中的 token。
 *
 * @author lxs
 * @date 2026-07-09
 */
public final class TokenResolveUtils {

    public static final String ACCESS_TOKEN_PARAMETER = "access_token";

    public static final String TOKEN_PARAMETER = "token";

    private TokenResolveUtils() {
    }

    /**
     * 从 Servlet 请求中解析 token；兼容请求头和请求参数。
     */
    public static String resolve(HttpServletRequest request) {
        String token = resolveAuthorizationValue(request.getHeader(Oauth2Constants.HEADER_TOKEN_KEY), true);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        token = resolveAuthorizationValue(request.getParameter(ACCESS_TOKEN_PARAMETER), false);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        token = resolveAuthorizationValue(request.getParameter(TOKEN_PARAMETER), false);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return resolveAuthorizationValue(request.getParameter(Oauth2Constants.HEADER_TOKEN_KEY), false);
    }

    /**
     * 从 WebFlux query 参数和请求头中解析 token。
     */
    public static String resolve(String authorization, MultiValueMap<String, String> queryParams) {
        String token = resolveAuthorizationValue(authorization, true);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        token = resolveAuthorizationValue(queryParams.getFirst(ACCESS_TOKEN_PARAMETER), false);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        token = resolveAuthorizationValue(queryParams.getFirst(TOKEN_PARAMETER), false);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return resolveAuthorizationValue(queryParams.getFirst(Oauth2Constants.HEADER_TOKEN_KEY), false);
    }

    /**
     * 解析单个 token 值；请求头只接受 Bearer，query 参数允许裸 token。
     */
    public static String resolveAuthorizationValue(String value, boolean header) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        if (StrUtil.startWithIgnoreCase(value, CommonConstants.AUTHORIZATION_BEARER + " ")) {
            return TokenUtils.getToken(value);
        }
        return header ? null : value;
    }

}
