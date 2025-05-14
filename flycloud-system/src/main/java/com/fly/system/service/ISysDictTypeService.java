package com.fly.system.service;

import com.fly.system.api.domain.SysDictType;
import com.fly.system.api.domain.vo.SysDictTypeVo;
import com.fly.system.api.domain.bo.SysDictTypeBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 字典类型 接口层
 *
 * @author fly
 * @date 2024-12-08
 */
public interface ISysDictTypeService {

    /**
     * 查询字典类型
     */
    SysDictTypeVo queryById(Long id);

    /**
     * 查询字典类型分页列表
     */
    PageVo<SysDictTypeVo> queryPageList(SysDictTypeBo bo, PageBo pageBo);

    /**
     * 查询字典类型列表
     */
    List<SysDictTypeVo> queryList(SysDictTypeBo bo);

    /**
     * 新增/修改字典类型
     */
    Boolean saveOrUpdate(SysDictTypeBo bo);

    /**
     * 修改字典类型
     */
    Boolean updateByBo(SysDictTypeBo bo);

    /**
     * 校验并批量删除字典类型信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
