package com.fly.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.system.api.domain.SysRolePermission;

/**
 * 角色权限表 服务类
 *
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 根据角色查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
//    List<String> getMenuIdByRoleId(String roleId);

}
