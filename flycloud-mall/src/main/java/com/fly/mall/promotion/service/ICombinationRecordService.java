package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CombinationRecordBo;
import com.fly.mall.api.promotion.domain.vo.CombinationRecordVo;

import java.util.Collection;
import java.util.List;

/**
 * 拼团记录 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface ICombinationRecordService {

    /**
     * 查询拼团记录详情。
     */
    CombinationRecordVo queryById(Long id);

    /**
     * 分页查询拼团记录。
     */
    PageVo<CombinationRecordVo> queryPageList(CombinationRecordBo bo, PageBo pageBo);

    /**
     * 查询拼团记录列表。
     */
    List<CombinationRecordVo> queryList(CombinationRecordBo bo);

    /**
     * 新增或修改拼团记录。
     */
    Boolean saveOrUpdate(CombinationRecordBo bo);

    /**
     * 校验并批量删除拼团记录。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
