package com.fly.common.admain.bo;

import lombok.Data;

/**
 * 认证中心登录参数
 *
 * @author lxs
 * @date 2023/5/2
 */
@Data
public class AuthLoginBo {

    /**
     * 授权模式 (四种: authorization_code, password, refresh_token, client_credentials)
     */
    private String grant_type = "password";

    /**
     * Oauth2客户端ID
     */
    private String client_id;

    /**
     * Oauth2客户端秘钥
     */
    private String client_secret;

    /**
     *
     */
    private String refresh_token;

    /**
     * 系统用户名
     */
    private String username;

    /**
     * 系统用户密码
     */
    private String password;

}
