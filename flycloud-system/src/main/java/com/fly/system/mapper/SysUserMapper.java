package com.fly.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.system.api.system.domain.SysUser;
import com.fly.system.api.system.domain.bo.SysUserBo;
import com.fly.system.api.system.domain.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author fly
 * @date 2026-08-31
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

    /**
     * 查询拥有指定角色标识的未删除后台用户，用于运维类通知。
     */
    @Select("""
            SELECT DISTINCT u.id
            FROM sys_user u
            INNER JOIN sys_user_role ur ON ur.user_id = u.id
            INNER JOIN sys_role r ON r.id = ur.role_id
            WHERE r.code = #{roleCode} AND u.is_deleted = 0 AND r.is_deleted = 0
            """)
    List<Long> selectUserIdsByRoleCode(@Param("roleCode") String roleCode);

}
