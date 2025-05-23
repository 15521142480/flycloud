package com.fly.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.system.api.domain.SysMenu;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import com.fly.system.api.domain.vo.SysMenuVo;
import com.fly.system.api.domain.bo.SysMenuBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 菜单Service接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface ISysMenuService extends IService<SysMenu> {


    /**
     * 查询菜单列表
     */
    List<SysMenuVo> queryList(SysMenuBo bo);

    List<SysMenuTreeVo> getAllList(SysMenuBo bo);

    /**
     * 查询菜单列表 - 树型
     */
    List<SysMenuTreeVo> getTreeList(SysMenuBo bo);


    /**
     * 新增/修改菜单
     */
    int saveOrUpdate(SysMenuBo bo);


    /**
     * 查询菜单
     */
    SysMenuVo queryById(Long id);

    /**
     * 查询菜单列表 - 分页
     */
    PageVo<SysMenuVo> queryPageList(SysMenuBo bo, PageBo pageBo);


    /**
     * 修改菜单
     */
    Boolean insertByBo(SysMenuBo bo);

    /**
     * 修改菜单
     */
    Boolean updateByBo(SysMenuBo bo);

    /**
     * 校验并批量删除菜单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据用户获取菜单树数据列表
     */
    List<SysMenuTreeVo> getMenuTreeListByUserId(Long id);
}
