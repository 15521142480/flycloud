package com.fly.auth.domain.vo;

import lombok.Data;

/**
 * 授权用户存放token信息
 *
 * 放关键信息即可，放多影响token长度
 *
 * @author: lxs
 * @date: 2026/7/5
 */
@Data
public class AuthTokenClaimsVo {

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
     * 用户角色id
     */
    public String roleIds;


}
