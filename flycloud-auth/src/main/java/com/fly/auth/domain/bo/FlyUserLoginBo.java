package com.fly.auth.domain.bo;

import lombok.Data;

/**
 * 系统用户登录-bo
 *
 * @author lxs
 * @date 2023/5/2
 */
@Data
public class FlyUserLoginBo {


    /**
     * 授权类型
     */
    private String grant_type;

    /**
     * 授权类型
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 使用范围
     */
    private String scope;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
     */
    private int loginType;




}
