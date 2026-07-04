package com.fly.auth.service;

import com.fly.auth.domain.vo.AuthLoginRespVo;

import java.util.Map;

/**
 * 认证服务
 *
 * @author lxs
 */
public interface AuthService {

    /**
     * 创建访问令牌
     */
    AuthLoginRespVo token(Map<String, String> loginParam);


    /**
     * 刷新令牌
     */
    AuthLoginRespVo refreshToken(String refreshToken);

    /**
     * 注销访问令牌
     */
    void revokeToken(String token);
}
