package com.fly.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flycloud.system.api.entity.SysUser;

/**
 * 用户 - 接口层
 *
 * @author: lxs
 * @date: 2024/8/16
 */
public interface ISysUserService extends IService<SysUser> {


    /**
     * 查询用户信息
     *
     * @param sysUser 用户
     * @return 用户对象
     */
    SysUser getOneIgnoreTenant(SysUser sysUser);



}
