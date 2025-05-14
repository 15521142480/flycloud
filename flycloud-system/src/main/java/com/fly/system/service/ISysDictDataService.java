package com.fly.system.service;

import com.fly.system.api.domain.vo.SysDictDataVo;
import com.fly.system.api.domain.bo.SysDictDataBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * 字典数据Service接口
 *
 * @author fly
 * @date 2024-11-23
 */
public interface ISysDictDataService {

    /**
     * 查询字典数据
     */
    SysDictDataVo queryById(Long id);

    /**
     * 查询字典数据列表
     */
    PageVo<SysDictDataVo> queryPageList(SysDictDataBo bo, PageBo pageBo);

    /**
     * 查询字典数据列表
     */
    List<SysDictDataVo> queryList(SysDictDataBo bo);

    /**
     * 新增/修改字典数据
     */
    Boolean saveOrUpdate(SysDictDataBo bo);


    /**
     * 校验并批量删除字典数据信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
