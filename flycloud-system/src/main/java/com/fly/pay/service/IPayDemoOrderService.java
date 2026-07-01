package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.PayDemoOrder;
import com.fly.system.api.pay.domain.bo.PayDemoOrderCreateReqBo;
import com.fly.system.api.pay.domain.vo.PayDemoOrderVo;

/**
 * 支付示例订单 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayDemoOrderService {

    /**
     * 创建支付示例订单。
     */
    Long createDemoOrder(Long userId, PayDemoOrderCreateReqBo createReqBo, String userIp);

    /**
     * 查询支付示例订单。
     */
    PayDemoOrder getDemoOrder(Long id);

    /**
     * 分页查询支付示例订单。
     */
    PageVo<PayDemoOrderVo> getDemoOrderPage(PageBo pageBo);

    /**
     * 更新支付示例订单为已支付。
     */
    void updateDemoOrderPaid(Long id, Long payOrderId);

    /**
     * 发起支付示例订单退款。
     */
    void refundDemoOrder(Long id, String userIp);

    /**
     * 更新支付示例订单为已退款。
     */
    void updateDemoOrderRefunded(Long id, String refundId, Long payRefundId);

}
