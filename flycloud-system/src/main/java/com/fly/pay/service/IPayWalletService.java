package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.PayWalletTransaction;
import com.fly.system.api.pay.domain.bo.PayWalletBo;
import com.fly.system.api.pay.domain.vo.PayWalletRespVo;

/**
 * 支付钱包 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayWalletService {

    /**
     * 获取或创建用户钱包。
     */
    PayWallet getOrCreateWallet(Long userId, Integer userType);

    /**
     * 根据编号查询钱包。
     */
    PayWallet getWallet(Long id);

    /**
     * 分页查询钱包。
     */
    PageVo<PayWalletRespVo> queryPageList(PayWalletBo bo, PageBo pageBo);

    /**
     * 扣减钱包余额并生成流水。
     */
    PayWalletTransaction reduceWalletBalance(Long walletId, String bizId, PayWalletBizTypeEnum bizType, Integer price);

    /**
     * 增加钱包余额并生成流水。
     */
    PayWalletTransaction addWalletBalance(Long walletId, String bizId, PayWalletBizTypeEnum bizType, Integer price);

    /**
     * 冻结钱包金额。
     */
    void freezePrice(Long walletId, Integer price);

    /**
     * 解冻钱包金额。
     */
    void unfreezePrice(Long walletId, Integer price);

}
