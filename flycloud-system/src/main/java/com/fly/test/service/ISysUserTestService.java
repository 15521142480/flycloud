package com.fly.test.service;

import com.fly.test.domain.vo.SysUserTestVo;
import com.fly.test.domain.bo.SysUserTestBo;
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
    SysUserTestVo queryById(Long userId);

    /**
     * 查询用户信息列表
     */
    PageVo<SysUserTestVo> queryPageList(SysUserTestBo bo, PageBo pageBo);

    /**
     * 查询用户信息列表
     */
    List<SysUserTestVo> queryList(SysUserTestBo bo);

    /**
     * 修改用户信息
     */
    Boolean insertByBo(SysUserTestBo bo);

    /**
     * 修改用户信息
     */
    Boolean updateByBo(SysUserTestBo bo);

    /**
     * 校验并批量删除用户信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);



}
