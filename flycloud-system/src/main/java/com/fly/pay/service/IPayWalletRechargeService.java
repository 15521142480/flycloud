package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.AppPayWalletRechargeCreateReqVo;
import com.fly.system.api.pay.domain.bo.PayWalletRechargeBo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeCreateRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargeVo;

/**
 * 支付钱包充值记录 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayWalletRechargeService {

    /**
     * 创建钱包充值记录。
     */
    AppPayWalletRechargeCreateRespVo createWalletRecharge(Long userId, Integer userType, String userIp,
                                                          AppPayWalletRechargeCreateReqVo reqVo);

    /**
     * 查询移动端钱包充值分页。
     */
    PageVo<AppPayWalletRechargeRespVo> getAppWalletRechargePage(Long userId, Integer userType, PageBo pageBo);

    /**
     * 支付成功后回充钱包余额。
     */
    void updateWalletRechargePaid(Long payOrderId, String channelCode);

    /**
     * 支付回调后更新钱包充值为已支付。
     */
    void updateWalletRechargerPaid(Long id, Long payOrderId);

    /**
     * 发起钱包充值退款。
     */
    void refundWalletRecharge(Long id, String userIp);

    /**
     * 退款回调后更新钱包充值为已退款。
     */
    void updateWalletRechargeRefunded(Long id, String refundId, Long payRefundId);

    /**
     * 分页查询钱包充值记录。
     */
    PageVo<PayWalletRechargeVo> queryPageList(PayWalletRechargeBo bo, PageBo pageBo);

}
