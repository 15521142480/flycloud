package com.fly.system.service;

import com.fly.system.api.domain.SysRole;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import com.fly.system.api.domain.vo.SysRoleVo;
import com.fly.system.api.domain.bo.SysRoleBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 角色Service接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface ISysRoleService {

    /**
     * 查询角色
     */
    SysRoleVo queryById(Long id);

    /**
     * 查询角色列表
     */
    PageVo<SysRoleVo> queryPageList(SysRoleBo bo, PageBo pageBo);

    /**
     * 查询角色菜单权限列表 - 树型
     */
    List<SysMenuTreeVo> getRoleTreeList(Long id);

    /**
     * 新增/修改角色
     */
    int saveOrUpdate(SysRoleBo bo);

    /**
     * 修改角色信息
     */
    int updateById(SysRoleBo bo);

    /**
     * 查询角色列表
     */
    List<SysRoleVo> queryList(SysRoleBo bo);

    /**
     * 修改角色
     */
    Boolean insertByBo(SysRoleBo bo);

    /**
     * 修改角色
     */
    Boolean updateByBo(SysRoleBo bo);

    /**
     * 校验并批量删除角色信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
