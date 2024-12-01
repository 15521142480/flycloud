package com.fly.system.service;

import com.fly.system.api.domain.vo.SysUserRoleVo;
import com.fly.system.api.domain.bo.SysUserRoleBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色Service接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface ISysUserRoleService {

    /**
     * 查询用户角色
     */
    SysUserRoleVo queryById(Long id);

    /**
     * 查询用户角色列表
     */
    PageVo<SysUserRoleVo> queryPageList(SysUserRoleBo bo, PageBo pageBo);

    /**
     * 查询用户角色列表
     */
    List<SysUserRoleVo> queryList(SysUserRoleBo bo);

    /**
     * 修改用户角色
     */
    Boolean insertByBo(SysUserRoleBo bo);

    /**
     * 修改用户角色
     */
    Boolean updateByBo(SysUserRoleBo bo);

    /**
     * 校验并批量删除用户角色信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
