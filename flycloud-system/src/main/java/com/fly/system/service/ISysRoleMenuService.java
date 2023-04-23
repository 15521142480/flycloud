package com.fly.system.service;

import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.system.domain.bo.SysRoleMenuBo;
import com.fly.system.domain.vo.SysRoleMenuVo;

import java.util.Collection;
import java.util.List;

/**
 * 角色和菜单关联Service接口
 *
 * @author fly
 * @date 2023-04-23
 */
public interface ISysRoleMenuService {

    /**
     * 查询角色和菜单关联
     */
    SysRoleMenuVo queryById(Long roleId);

    /**
     * 查询角色和菜单关联列表
     */
    PageVo<SysRoleMenuVo> queryPageList(SysRoleMenuBo bo, PageBo pageBo);

    /**
     * 查询角色和菜单关联列表
     */
    List<SysRoleMenuVo> queryList(SysRoleMenuBo bo);

    /**
     * 修改角色和菜单关联
     */
    Boolean insertByBo(SysRoleMenuBo bo);

    /**
     * 修改角色和菜单关联
     */
    Boolean updateByBo(SysRoleMenuBo bo);

    /**
     * 校验并批量删除角色和菜单关联信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
