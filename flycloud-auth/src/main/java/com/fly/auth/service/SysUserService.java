package com.fly.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 系统用户模块-接口层
 *
 * @author lxs
 * @date 2023/4/28
 */
public interface SysUserService {


    UserDetails getUserAuthInfo(String userName);

}
