package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryPickUpStoreBo;
import com.fly.mall.api.trade.domain.vo.DeliveryPickUpStoreVo;

import java.util.Collection;
import java.util.List;

/**
 * 自提门店 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IDeliveryPickUpStoreService {

    /**
     * 查询自提门店详情。
     */
    DeliveryPickUpStoreVo queryById(Long id);

    /**
     * 分页查询自提门店。
     */
    PageVo<DeliveryPickUpStoreVo> queryPageList(DeliveryPickUpStoreBo bo, PageBo pageBo);

    /**
     * 查询自提门店列表。
     */
    List<DeliveryPickUpStoreVo> queryList(DeliveryPickUpStoreBo bo);

    /**
     * 新增或修改自提门店。
     */
    Boolean saveOrUpdate(DeliveryPickUpStoreBo bo);

    /**
     * 校验并批量删除自提门店。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
