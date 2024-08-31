package com.fly.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.common.database.entity.Search;
import com.fly.system.api.domain.SysMenu;
import com.fly.system.api.poi.SysMenuPOI;
import com.fly.system.api.vo.SysMenuVO;

import java.util.List;

/**
 * 菜单权限表 服务类
 *
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 菜单路由
     *
     * @param roleId 角色ID
     * @return List
     */
    List<SysMenuVO> routes(String roleId);

    /**
     * 查询列表
     *
     * @param search 检索对象
     * @return List
     */
    List<SysMenu> searchList(Search search);

    /**
     * 保存全部
     *
     * @param sysMenu 菜单对象
     * @return boolean
     */
    boolean saveAll(SysMenu sysMenu);

    /**
     * 状态设置
     *
     * @param ids    id列表
     * @param status 状态
     * @return boolean
     */
    boolean status(String ids, String status);

    /**
     * 菜单导出
     *
     * @return 菜单导出
     */
    List<SysMenuPOI> export();

    /**
     * 检查是否存在菜单子节点
     *
     * @param id 菜单ID
     * @return 是否
     */
    boolean checkChild(Long id);

    /**
     * 根据菜单ID查询父类ID
     *
     * @param menuId 菜单ID
     * @return parentId
     */
    Long getMenuChild(Long menuId);

}
