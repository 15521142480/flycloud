package com.fly.system.mapper;

import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysRole;
import com.fly.system.api.domain.vo.SysRoleVo;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface SysRoleMapper extends BaseMapperPlus<SysRoleMapper, SysRole, SysRoleVo> {

    /**
     * 根据用户查询权限信息
     */
    List<String> selectPermissionListByUserId(Long userId);

    /**
     * 根据用户查询角色信息列表
     */
    List<String> selectRoleIdListByUserId(Long userId);
    List<String> getRoleNameListByUserId(Long userId);
}
