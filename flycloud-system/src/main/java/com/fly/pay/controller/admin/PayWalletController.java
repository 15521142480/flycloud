package com.fly.pay.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.pay.service.IPayWalletService;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.bo.PayWalletBo;
import com.fly.system.api.pay.domain.vo.PayWalletRespVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付钱包控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/wallet")
public class PayWalletController {

    private final IPayWalletService walletService;

    /**
     * 查询支付钱包分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet:balance:list')")
    @GetMapping("/list")
    public R<PageVo<PayWalletRespVo>> list(PayWalletBo bo, PageBo pageBo) {
        return R.ok(walletService.queryPageList(bo, pageBo));
    }

    /**
     * 查询支付钱包分页列表，兼容 yudao 前端接口。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet:balance:list')")
    @GetMapping("/page")
    public R<PageVo<PayWalletRespVo>> page(PayWalletBo bo, PageBo pageBo) {
        return R.ok(walletService.queryPageList(bo, pageBo));
    }

    /**
     * 获取支付钱包详情。
     */
//    @PreAuthorize("@pms.hasPermission('pay:wallet:balance:query')")
    @GetMapping("/get")
    public R<PayWalletRespVo> get(PayWalletBo bo) {
        PayWallet wallet = bo.getId() != null ? walletService.getWallet(bo.getId())
                : walletService.getOrCreateWallet(bo.getUserId(), bo.getUserType() == null ? 2 : bo.getUserType());
        return R.ok(BeanUtil.toBean(wallet, PayWalletRespVo.class));
    }

    /**
     * 更新会员用户余额。
     */
    @PreAuthorize("@pms.hasAnyPermission({'member:user:update-balance', 'pay:wallet:balance:update-balance'})")
    @PutMapping("/update-balance")
    public R<Boolean> updateBalance(@RequestBody UpdateBalanceReq req) {
        PayWallet wallet = walletService.getOrCreateWallet(req.getUserId(), 2);
        walletService.addWalletBalance(wallet.getId(), String.valueOf(req.getUserId()),
                PayWalletBizTypeEnum.UPDATE_BALANCE, req.getBalance());
        return R.result(true);
    }

    /**
     * 钱包余额调整请求。
     */
    @Data
    public static class UpdateBalanceReq {
        private Long userId;
        private Integer balance;
    }

}
