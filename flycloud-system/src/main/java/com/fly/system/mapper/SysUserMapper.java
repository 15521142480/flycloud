package com.fly.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysUserVo;

/**
 * 用户Mapper接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUserVo> {

    /**
     * 忽略租户信息
     * @param sysUser
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectOneIgnoreTenant(SysUser sysUser);

}
