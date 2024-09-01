package com.fly.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.domain.bo.SysUserBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author fly
 * @date 2024-08-31
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 查询用户
     */
    SysUserVo queryById(Long id);

    /**
     * 查询用户列表
     */
    PageVo<SysUserVo> queryPageList(SysUserBo bo, PageBo pageBo);

    /**
     * 用户新增/修改
     */
    int saveOrUpdate(SysUserBo bo);

    /**
     * 重置密码
     */
    int resetPassword(Long id);

    /**
     * 查询用户列表
     */
    List<SysUserVo> queryList(SysUserBo bo);

    /**
     * 修改用户
     */
    Boolean insertByBo(SysUserBo bo);

    /**
     * 修改用户
     */
    Boolean updateByBo(SysUserBo bo);

    /**
     * 校验并批量删除用户信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    // ==============================================================================

    /**
     * 用户状态设置
     *
     * @param ids    id列表串
     * @param status 状态
     * @return boolean
     */
//    boolean status(String ids, String status);


    /**
     * 业务分页
     *
     * @param search  分页搜索
     * @param sysUser 部门参数
     * @return 分页列表
     */
//    IPage<SysUser> listPage(Search search, SysUser sysUser);


    /**
     * 忽略租户查询用户信息
     *
     * @param sysUser 用户对象
     * @return 用户对象
     */
    SysUser getOneIgnoreTenant(SysUser sysUser);


    /**
     * 导出列表
     *
     * @return 导出POI数据
     */
//    List<SysUserPOI> export();

}
