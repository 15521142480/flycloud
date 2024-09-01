package com.fly.system.service;

import com.fly.system.api.domain.SysRoleMenu;
import com.fly.system.api.domain.vo.SysRoleMenuVo;
import com.fly.system.api.domain.bo.SysRoleMenuBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 角色权限Service接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface ISysRoleMenuService {

    /**
     * 查询角色权限
     */
    SysRoleMenuVo queryById(Long id);

    /**
     * 查询角色权限列表
     */
    PageVo<SysRoleMenuVo> queryPageList(SysRoleMenuBo bo, PageBo pageBo);

    /**
     * 查询角色权限列表
     */
    List<SysRoleMenuVo> queryList(SysRoleMenuBo bo);

    /**
     * 修改角色权限
     */
    Boolean insertByBo(SysRoleMenuBo bo);

    /**
     * 修改角色权限
     */
    Boolean updateByBo(SysRoleMenuBo bo);

    /**
     * 校验并批量删除角色权限信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
