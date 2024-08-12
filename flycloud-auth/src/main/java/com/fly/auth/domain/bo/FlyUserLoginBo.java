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




}
