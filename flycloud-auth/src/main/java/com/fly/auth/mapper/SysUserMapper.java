package com.fly.auth.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flycloud.system.api.entity.SysUser;

/**
 * 用户表 Mapper 接口
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 用户信息
     *
     * @param sysUser
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectOneIgnoreTenant(SysUser sysUser);

}
