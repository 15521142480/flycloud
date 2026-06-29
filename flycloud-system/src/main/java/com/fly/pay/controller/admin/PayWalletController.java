package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletService;
import com.fly.system.api.pay.domain.bo.PayWalletBo;
import com.fly.system.api.pay.domain.vo.PayWalletRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付钱包控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/wallet")
public class PayWalletController {

    private final IPayWalletService walletService;

    /**
     * 查询支付钱包分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet:list')")
    @GetMapping("/list")
    public R<PageVo<PayWalletRespVo>> list(PayWalletBo bo, PageBo pageBo) {
        return R.ok(walletService.queryPageList(bo, pageBo));
    }

}
