package com.fly.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据账号查询条数
     */
    int selectCountByAccount(@Param("account") String account, @Param("id") Long id);

    /**
     * 根据呢称查询条数
     */
    int selectCountByName(@Param("name") String account, @Param("id") Long id);
}
