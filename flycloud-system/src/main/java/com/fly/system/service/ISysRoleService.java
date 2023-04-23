package com.fly.system.service;

import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.system.domain.bo.SysRoleBo;
import com.fly.system.domain.vo.SysRoleVo;

import java.util.Collection;
import java.util.List;

/**
 * 角色信息Service接口
 *
 * @author fly
 * @date 2023-04-23
 */
public interface ISysRoleService {

    /**
     * 查询角色信息
     */
    SysRoleVo queryById(Long roleId);

    /**
     * 查询角色信息列表
     */
    PageVo<SysRoleVo> queryPageList(SysRoleBo bo, PageBo pageBo);

    /**
     * 查询角色信息列表
     */
    List<SysRoleVo> queryList(SysRoleBo bo);

    /**
     * 修改角色信息
     */
    Boolean insertByBo(SysRoleBo bo);

    /**
     * 修改角色信息
     */
    Boolean updateByBo(SysRoleBo bo);

    /**
     * 校验并批量删除角色信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
