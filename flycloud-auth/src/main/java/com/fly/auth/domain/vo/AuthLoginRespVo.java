package com.fly.auth.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 授权登录响应信息
 *
 * @author: lxs
 * @date: 2026/7/5
 */
@Data
public class AuthLoginRespVo {

    /**
     * token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * jti 唯一编号
     *
     * token = 一整张身份证
     * jti   = 身份证编号
     */
    private String jti;

    /**
     * 过期时间
     */
    public Long expiresTime;

    /**
     * 用户id
     */
    public Long userId;

    /**
     * 用户名称
     */
    public String userName;

    /**
     * 手机
     */
    public String mobile;

    /**
     * 用户类型
     */
    public Integer userType;

    /**
     * 登录类型
     */
    public Integer loginType;


    /**
     * 部门id
     */
    public String deptId;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 用户角色id
     */
    public String roleIds;

    /**
     * 权限
     */
    public List<String> authorities;
//    public String authorities;

    
}
