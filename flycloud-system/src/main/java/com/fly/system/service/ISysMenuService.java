package com.fly.system.service;

import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.system.domain.bo.SysMenuBo;
import com.fly.system.domain.vo.SysMenuVo;

import java.util.Collection;
import java.util.List;

/**
 * 菜单权限Service接口
 *
 * @author fly
 * @date 2023-04-23
 */
public interface ISysMenuService {

    /**
     * 查询菜单权限
     */
    SysMenuVo queryById(Long menuId);

    /**
     * 查询菜单权限列表
     */
    PageVo<SysMenuVo> queryPageList(SysMenuBo bo, PageBo pageBo);

    /**
     * 查询菜单权限列表
     */
    List<SysMenuVo> queryList(SysMenuBo bo);

    /**
     * 修改菜单权限
     */
    Boolean insertByBo(SysMenuBo bo);

    /**
     * 修改菜单权限
     */
    Boolean updateByBo(SysMenuBo bo);

    /**
     * 校验并批量删除菜单权限信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
