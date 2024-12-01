package com.fly.system.service;

import com.fly.system.api.domain.vo.SysDictVo;
import com.fly.system.api.domain.bo.SysDictBo;
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
public interface ISysDictService {

    /**
     * 查询字典数据
     */
    SysDictVo queryById(Long id);

    /**
     * 查询字典数据列表
     */
    PageVo<SysDictVo> queryPageList(SysDictBo bo, PageBo pageBo);

    /**
     * 查询字典数据列表
     */
    List<SysDictVo> queryList(SysDictBo bo);

    /**
     * 修改字典数据
     */
    Boolean insertByBo(SysDictBo bo);

    /**
     * 修改字典数据
     */
    Boolean updateByBo(SysDictBo bo);

    /**
     * 校验并批量删除字典数据信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
