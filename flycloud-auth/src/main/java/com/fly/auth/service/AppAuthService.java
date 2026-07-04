package com.fly.auth.service;


import com.fly.auth.domain.vo.AppAuthLoginReqVo;
import com.fly.auth.domain.vo.AppAuthLoginRespVo;

/**
 * App认证服务
 *
 * @author: lxs
 * @date: 2026/7/4
 */
public interface AppAuthService {


    /**
     * 登录：手机 + 密码
     */
    AppAuthLoginRespVo login(AppAuthLoginReqVo appAuthLoginReqVo);


    /**
     * 刷新令牌
     */
    AppAuthLoginRespVo refreshToken(String refreshToken);


    /**
     * 登出
     */
    void logout(String token);

}
