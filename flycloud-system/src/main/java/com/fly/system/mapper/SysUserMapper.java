package com.fly.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.bo.SysUserBo;
import com.fly.system.api.domain.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUserVo> {

    /**
     * 查询所有用户精简版
     */
    List<SysUserVo> selectAllListSimple(SysUserBo bo);

    /**
     * 根据用户信息查询用户
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectOneUserByUser(SysUser sysUser);

    /**
     * 根据账号查询条数
     */
    int selectCountByAccount(@Param("account") String account, @Param("id") Long id);

    /**
     * 根据呢称查询条数
     */
    int selectCountByName(@Param("name") String account, @Param("id") Long id);

}
