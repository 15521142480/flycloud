package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletTransactionService;
import com.fly.system.api.pay.domain.bo.PayWalletTransactionBo;
import com.fly.system.api.pay.domain.vo.PayWalletTransactionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付钱包流水控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/wallet-transaction")
public class PayWalletTransactionController {

    private final IPayWalletTransactionService walletTransactionService;

    /**
     * 查询支付钱包流水分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-transaction:list')")
    @GetMapping("/list")
    public R<PageVo<PayWalletTransactionVo>> list(PayWalletTransactionBo bo, PageBo pageBo) {
        return R.ok(walletTransactionService.queryPageList(bo, pageBo));
    }

}
