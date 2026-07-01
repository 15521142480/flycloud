package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.TradeOrderBo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateRespVo;
import com.fly.mall.api.trade.domain.vo.AppOrderExpressTrackRespDto;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderDetailRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderItemCommentCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderItemRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderPageItemRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeProductSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderSummaryRespVo;
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
     * 分页查询当前用户移动端交易订单。
     */
    PageVo<AppTradeOrderPageItemRespVo> queryUserAppPageList(Long userId, TradeOrderBo bo, PageBo pageBo);

    /**
     * 查询当前用户移动端交易订单详情。
     */
    AppTradeOrderDetailRespVo queryUserAppDetail(Long userId, Long id);

    /**
     * 查询当前用户移动端订单物流轨迹。
     */
    List<AppOrderExpressTrackRespDto> getAppExpressTrackList(Long userId, Long id);

    /**
     * 查询后台订单物流轨迹。
     */
    List<AppOrderExpressTrackRespDto> getExpressTrackList(Long id);

    /**
     * 查询后台订单统计。
     */
    TradeOrderSummaryRespVo getOrderSummary(TradeOrderBo bo);

    /**
     * 查询当前用户移动端交易订单项。
     */
    AppTradeOrderItemRespVo getAppOrderItem(Long userId, Long id);

    /**
     * 当前用户创建交易订单项评价。
     */
    Long createOrderItemComment(Long userId, AppTradeOrderItemCommentCreateReqVo createReqVo);

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
     * 后台订单发货。
     */
    Boolean deliveryOrder(TradeOrderBo bo);

    /**
     * 后台更新订单备注。
     */
    Boolean updateOrderRemark(TradeOrderBo bo);

    /**
     * 后台更新订单价格。
     */
    Boolean updateOrderPrice(TradeOrderBo bo);

    /**
     * 后台更新订单收货地址。
     */
    Boolean updateOrderAddress(TradeOrderBo bo);

    /**
     * 后台按订单编号核销订单。
     */
    Boolean pickUpOrderByAdmin(Long userId, Long id);

    /**
     * 后台按核销码核销订单。
     */
    Boolean pickUpOrderByAdmin(Long userId, String pickUpVerifyCode);

    /**
     * 根据自提核销码查询订单。
     */
    TradeOrderVo getByPickUpVerifyCode(String pickUpVerifyCode);

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
