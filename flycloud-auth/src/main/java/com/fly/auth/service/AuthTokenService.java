package com.fly.auth.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Token 认证服务
 *
 * @author lxs
 */
public interface AuthTokenService {

    /**
     * 创建访问令牌
     */
    Map<String, Object> createToken(Map<String, String> loginParam, HttpServletRequest request);

    /**
     * 注销访问令牌
     */
    void revokeToken(String token);
}
