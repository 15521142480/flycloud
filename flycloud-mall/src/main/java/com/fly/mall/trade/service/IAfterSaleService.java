package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.AfterSaleBo;
import com.fly.mall.api.domain.trade.vo.AfterSaleVo;

import java.util.Collection;
import java.util.List;

/**
 * 售后单 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IAfterSaleService {

    /**
     * 查询售后单详情。
     */
    AfterSaleVo queryById(Long id);

    /**
     * 分页查询售后单。
     */
    PageVo<AfterSaleVo> queryPageList(AfterSaleBo bo, PageBo pageBo);

    /**
     * 查询售后单列表。
     */
    List<AfterSaleVo> queryList(AfterSaleBo bo);

    /**
     * 新增或修改售后单。
     */
    Boolean saveOrUpdate(AfterSaleBo bo);

    /**
     * 校验并批量删除售后单。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
