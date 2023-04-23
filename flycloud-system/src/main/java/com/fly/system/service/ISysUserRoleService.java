package com.fly.system.service;

import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.system.domain.bo.SysUserRoleBo;
import com.fly.system.domain.vo.SysUserRoleVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户和角色关联Service接口
 *
 * @author fly
 * @date 2023-04-23
 */
public interface ISysUserRoleService {

    /**
     * 查询用户和角色关联
     */
    SysUserRoleVo queryById(Long userId);

    /**
     * 查询用户和角色关联列表
     */
    PageVo<SysUserRoleVo> queryPageList(SysUserRoleBo bo, PageBo pageBo);

    /**
     * 查询用户和角色关联列表
     */
    List<SysUserRoleVo> queryList(SysUserRoleBo bo);

    /**
     * 修改用户和角色关联
     */
    Boolean insertByBo(SysUserRoleBo bo);

    /**
     * 修改用户和角色关联
     */
    Boolean updateByBo(SysUserRoleBo bo);

    /**
     * 校验并批量删除用户和角色关联信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
