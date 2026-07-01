package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.AppPayOrderSubmitReqVo;
import com.fly.system.api.pay.domain.bo.PayOrderBo;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.PayOrderExtension;
import com.fly.system.api.pay.domain.vo.AppPayOrderSubmitRespVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 支付订单 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayOrderService {

    /**
     * 创建支付订单。
     */
    Long createPayOrder(PayOrderCreateReqDto createReqDto);

    /**
     * 根据编号查询支付订单。
     */
    PayOrderRespVo getOrder(Long id);

    /**
     * 根据支付订单号查询支付订单。
     */
    PayOrderRespVo getOrder(String no);

    /**
     * 根据应用编号和商户订单号查询支付订单。
     */
    PayOrderRespVo getOrder(Long appId, String merchantOrderId);

    /**
     * 根据编号集合查询支付订单列表。
     */
    List<PayOrderRespVo> getOrderList(Collection<Long> ids);

    /**
     * 查询应用下的支付订单数量。
     */
    Long getOrderCountByAppId(Long appId);

    /**
     * 分页查询支付订单。
     */
    PageVo<PayOrderRespVo> queryPageList(PayOrderBo bo, PageBo pageBo);

    /**
     * 查询支付订单列表。
     */
    List<PayOrderRespVo> queryList(PayOrderBo bo);

    /**
     * 提交支付订单。
     */
    AppPayOrderSubmitRespVo submitOrder(Long userId, AppPayOrderSubmitReqVo submitReqVo, String userIp);

    /**
     * 支付渠道回调后更新支付订单。
     */
    void notifyOrder(Long channelId, Map<String, String> params, String body, Map<String, String> headers);

    /**
     * 增加支付订单已退款金额。
     */
    void updateOrderRefundPrice(Long id, Integer incrRefundPrice);

    /**
     * 修改待支付订单金额。
     */
    void updatePayOrderPrice(Long id, Integer payPrice);

    /**
     * 根据编号查询支付订单拓展单。
     */
    PayOrderExtension getOrderExtension(Long id);

    /**
     * 根据支付订单号查询支付订单拓展单。
     */
    PayOrderExtension getOrderExtensionByNo(String no);

    /**
     * 同步支付订单状态。
     */
    int syncOrder(LocalDateTime minCreateTime);

    /**
     * 静默同步单个支付订单状态。
     */
    void syncOrderQuietly(Long id);

    /**
     * 关闭已过期的待支付订单。
     */
    int expireOrder();

}
