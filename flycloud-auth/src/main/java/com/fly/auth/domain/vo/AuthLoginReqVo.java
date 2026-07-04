package com.fly.auth.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 授权登录请求信息 - vo
 *
 * @author: lxs
 * @date: 2027/7/4
 */
@Data
public class AuthLoginReqVo {


    private String account;

    private String password;


}
