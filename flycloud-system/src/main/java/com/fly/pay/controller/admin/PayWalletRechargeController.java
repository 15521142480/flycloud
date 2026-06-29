package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletRechargeService;
import com.fly.system.api.pay.domain.bo.PayWalletRechargeBo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付钱包充值记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/wallet-recharge")
public class PayWalletRechargeController {

    private final IPayWalletRechargeService walletRechargeService;

    /**
     * 查询支付钱包充值记录分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge:list')")
    @GetMapping("/list")
    public R<PageVo<PayWalletRechargeVo>> list(PayWalletRechargeBo bo, PageBo pageBo) {
        return R.ok(walletRechargeService.queryPageList(bo, pageBo));
    }

}
