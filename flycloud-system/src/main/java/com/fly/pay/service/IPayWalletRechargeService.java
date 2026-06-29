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
 * @date 2026-06-30
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
     * 分页查询钱包充值记录。
     */
    PageVo<PayWalletRechargeVo> queryPageList(PayWalletRechargeBo bo, PageBo pageBo);

}
