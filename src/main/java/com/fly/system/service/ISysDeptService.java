package com.fly.system.service;

import com.fly.system.api.domain.SysDept;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.bo.SysDeptBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 部门Service接口
 *
 * @author fly
 * @date 2024-12-01
 */
public interface ISysDeptService {

    /**
     * 查询部门
     */
    SysDeptVo queryById(Long id);

    /**
     * 查询部门列表
     */
    PageVo<SysDeptVo> queryPageList(SysDeptBo bo, PageBo pageBo);

    /**
     * 查询部门列表
     */
    List<SysDeptVo> queryList(SysDeptBo bo);

    /**
     * 修改部门
     */
    Boolean insertByBo(SysDeptBo bo);

    /**
     * 修改部门
     */
    Boolean updateByBo(SysDeptBo bo);

    /**
     * 校验并批量删除部门信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}