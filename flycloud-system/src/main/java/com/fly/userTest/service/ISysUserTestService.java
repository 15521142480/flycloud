package com.fly.userTest.service;

import com.fly.userTest.domain.vo.SysUserVo;
import com.fly.userTest.domain.bo.SysUserBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author fly
 * @date 2023-04-22
 */
public interface ISysUserTestService {

    /**
     * 查询用户信息
     */
    SysUserVo queryById(Long userId);

    /**
     * 查询用户信息列表
     */
    PageVo<SysUserVo> queryPageList(SysUserBo bo, PageBo pageBo);

    /**
     * 查询用户信息列表
     */
    List<SysUserVo> queryList(SysUserBo bo);

    /**
     * 修改用户信息
     */
    Boolean insertByBo(SysUserBo bo);

    /**
     * 修改用户信息
     */
    Boolean updateByBo(SysUserBo bo);

    /**
     * 校验并批量删除用户信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);



}
