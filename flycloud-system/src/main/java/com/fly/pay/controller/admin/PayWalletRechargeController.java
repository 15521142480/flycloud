package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletRechargeService;
import com.fly.system.api.pay.domain.bo.PayWalletRechargeBo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargeVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 后台 - 支付钱包充值记录控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/wallet-recharge")
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

    /**
     * 查询支付钱包充值记录分页列表，兼容 yudao 前端接口。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge:list')")
    @GetMapping("/page")
    public R<PageVo<PayWalletRechargeVo>> page(PayWalletRechargeBo bo, PageBo pageBo) {
        return R.ok(walletRechargeService.queryPageList(bo, pageBo));
    }

    /**
     * 更新钱包充值为已支付。
     */
    @PostMapping("/update-paid")
    public R<Void> updatePaid(@RequestBody PayOrderNotifyReq req) {
        walletRechargeService.updateWalletRechargerPaid(Long.valueOf(req.getMerchantOrderId()), req.getPayOrderId());
        return R.ok();
    }

    /**
     * 发起钱包充值退款。
     */
    @PostMapping("/refund")
    public R<Void> refund(@RequestParam("id") Long id, HttpServletRequest request) {
        walletRechargeService.refundWalletRecharge(id, getClientIp(request));
        return R.ok();
    }

    /**
     * 更新钱包充值为已退款。
     */
    @PostMapping("/update-refunded")
    public R<Void> updateRefunded(@RequestBody PayRefundNotifyReq req) {
        walletRechargeService.updateWalletRechargeRefunded(Long.valueOf(req.getMerchantOrderId()),
                req.getMerchantRefundId(), req.getPayRefundId());
        return R.ok();
    }

    /**
     * 支付订单通知请求。
     */
    @Data
    public static class PayOrderNotifyReq {
        private String merchantOrderId;
        private Long payOrderId;
        private String channelCode;
    }

    /**
     * 支付退款通知请求。
     */
    @Data
    public static class PayRefundNotifyReq {
        private String merchantOrderId;
        private String merchantRefundId;
        private Long payRefundId;
    }

    /**
     * 获取客户端 IP。
     */
    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}
