package com.fly.system.service;

import com.fly.system.api.domain.SysPost;
import com.fly.system.api.domain.vo.SysPostVo;
import com.fly.system.api.domain.bo.SysPostBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 岗位 接口层
 *
 * @author fly
 * @date 2026-03-02
 */
public interface ISysPostService {

    /**
     * 查询岗位
     */
    SysPostVo queryById(Long id);

    /**
     * 查询岗位分页列表
     */
    PageVo<SysPostVo> queryPageList(SysPostBo bo, PageBo pageBo);

    /**
     * 查询岗位列表
     */
    List<SysPostVo> queryList(SysPostBo bo);

    /**
     * 修改岗位
     */
    Boolean insertByBo(SysPostBo bo);

    /**
     * 修改岗位
     */
    Boolean updateByBo(SysPostBo bo);

    /**
     * 校验并批量删除岗位信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
