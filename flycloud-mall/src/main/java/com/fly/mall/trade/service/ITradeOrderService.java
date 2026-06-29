package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.TradeOrderBo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeProductSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;

import java.util.Collection;
import java.util.List;

/**
 * 交易订单 Service 接口。
 *
 * @author lxs
 * @date 2026-06-29
 */
public interface ITradeOrderService {

    /**
     * 查询交易订单详情。
     */
    TradeOrderVo queryById(Long id);

    /**
     * 查询当前用户交易订单详情。
     */
    TradeOrderVo queryByUserAndId(Long userId, Long id);

    /**
     * 分页查询交易订单。
     */
    PageVo<TradeOrderVo> queryPageList(TradeOrderBo bo, PageBo pageBo);

    /**
     * 分页查询当前用户交易订单。
     */
    PageVo<TradeOrderVo> queryUserPageList(Long userId, TradeOrderBo bo, PageBo pageBo);

    /**
     * 创建购物车订单。
     */
    TradeOrderVo createOrder(Long userId, TradeOrderBo bo);

    /**
     * 移动端结算订单。
     */
    AppTradeOrderSettlementRespVo settlementOrder(Long userId, AppTradeOrderSettlementReqVo settlementReqVo);

    /**
     * 移动端查询商品结算信息。
     */
    List<AppTradeProductSettlementRespVo> settlementProduct(List<Long> spuIds);

    /**
     * 移动端创建交易订单。
     */
    AppTradeOrderCreateRespVo createAppOrder(Long userId, AppTradeOrderCreateReqVo createReqVo);

    /**
     * 查询当前用户订单数量。
     */
    java.util.Map<String, Long> getOrderCount(Long userId);

    /**
     * 当前用户取消订单。
     */
    Boolean cancelOrder(Long userId, Long id);

    /**
     * 当前用户删除订单。
     */
    Boolean deleteOrder(Long userId, Long id);

    /**
     * 当前用户确认收货。
     */
    Boolean receiveOrder(Long userId, Long id);

    /**
     * 查询交易订单列表。
     */
    List<TradeOrderVo> queryList(TradeOrderBo bo);

    /**
     * 新增或修改交易订单。
     */
    Boolean saveOrUpdate(TradeOrderBo bo);

    /**
     * 校验并批量删除交易订单。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
