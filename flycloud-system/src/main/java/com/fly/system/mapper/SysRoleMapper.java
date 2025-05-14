package com.fly.system.mapper;

import com.fly.common.database.web.mapper.BaseMapperPlus;
import com.fly.system.api.domain.SysRole;
import com.fly.system.api.domain.vo.SysRoleVo;
import org.apache.ibatis.annotations.Param;

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
     * 含有某个角色编码的角色数量
     */
    int getRoleCountByUserAndCode(@Param("userId") Long userId, @Param("type") Long type, @Param("roleCode") String roleCode);

}
