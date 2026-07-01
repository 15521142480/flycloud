package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.system.api.pay.domain.PayWalletTransaction;
import com.fly.system.api.pay.domain.bo.AppPayWalletTransactionPageReqBo;
import com.fly.system.api.pay.domain.bo.PayWalletTransactionBo;
import com.fly.system.api.pay.domain.bo.WalletTransactionCreateReqBo;
import com.fly.system.api.pay.domain.vo.AppPayWalletTransactionRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletTransactionSummaryRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletTransactionVo;

import java.time.LocalDateTime;

/**
 * 支付钱包流水 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayWalletTransactionService {

    /**
     * 查询移动端钱包流水分页。
     */
    PageVo<AppPayWalletTransactionRespVo> getWalletTransactionPage(Long userId, Integer userType,
                                                                   AppPayWalletTransactionPageReqBo pageBo);

    /**
     * 查询钱包流水统计。
     */
    AppPayWalletTransactionSummaryRespVo getWalletTransactionSummary(Long userId, Integer userType,
                                                                     LocalDateTime[] createTime);

    /**
     * 创建钱包流水。
     */
    PayWalletTransaction createWalletTransaction(WalletTransactionCreateReqBo bo);

    /**
     * 根据业务查询钱包流水。
     */
    PayWalletTransaction getWalletTransaction(String bizId, PayWalletBizTypeEnum bizType);

    /**
     * 分页查询钱包流水。
     */
    PageVo<PayWalletTransactionVo> queryPageList(PayWalletTransactionBo bo, PageBo pageBo);

}
