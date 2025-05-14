package com.fly.system.mapper;

import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysUserRole;
import com.fly.system.api.domain.vo.SysUserRoleVo;

import java.util.List;

/**
 * 用户角色Mapper接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRoleVo> {



    /**
     * 根据用户查询角色信息列表
     */
    List<String> selectRoleIdListByUserId(Long userId);
    List<String> getRoleNameListByUserId(Long userId);

    List<Long> selectRoleIdsByUserId(Long userId);

}
