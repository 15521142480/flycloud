package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.TradeOrderItemBo;
import com.fly.mall.api.domain.trade.vo.TradeOrderItemVo;

import java.util.Collection;
import java.util.List;

/**
 * 交易订单项 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface ITradeOrderItemService {

    /**
     * 查询交易订单项详情。
     */
    TradeOrderItemVo queryById(Long id);

    /**
     * 分页查询交易订单项。
     */
    PageVo<TradeOrderItemVo> queryPageList(TradeOrderItemBo bo, PageBo pageBo);

    /**
     * 查询交易订单项列表。
     */
    List<TradeOrderItemVo> queryList(TradeOrderItemBo bo);

    /**
     * 根据订单编号查询订单项列表。
     */
    List<TradeOrderItemVo> queryListByOrderId(Long orderId);

    /**
     * 根据订单编号集合查询订单项列表。
     */
    List<TradeOrderItemVo> queryListByOrderIds(Collection<Long> orderIds);

    /**
     * 新增或修改交易订单项。
     */
    Boolean saveOrUpdate(TradeOrderItemBo bo);

    /**
     * 校验并批量删除交易订单项。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
