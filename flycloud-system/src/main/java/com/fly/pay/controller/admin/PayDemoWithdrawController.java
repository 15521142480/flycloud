package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.pay.service.IPayDemoWithdrawService;
import com.fly.system.api.pay.domain.bo.PayDemoWithdrawCreateReqBo;
import com.fly.system.api.pay.domain.vo.PayDemoWithdrawVo;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付示例提现单控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/demo-withdraw")
public class PayDemoWithdrawController {

    private final IPayDemoWithdrawService payDemoWithdrawService;

    /**
     * 创建支付示例提现单。
     */
    @PostMapping("/create")
    public R<Long> createDemoWithdraw(@Valid @RequestBody PayDemoWithdrawCreateReqBo createReqBo) {
        return R.ok(payDemoWithdrawService.createDemoWithdraw(createReqBo));
    }

    /**
     * 发起支付示例提现单转账。
     */
    @PostMapping("/transfer")
    public R<Long> transferDemoWithdraw(@RequestParam("id") Long id, HttpServletRequest request) {
        return R.ok(payDemoWithdrawService.transferDemoWithdraw(id, UserUtils.getCurUserId(), getClientIp(request)));
    }

    /**
     * 分页查询支付示例提现单。
     */
    @GetMapping("/page")
    public R<PageVo<PayDemoWithdrawVo>> getDemoWithdrawPage(PageBo pageBo) {
        return R.ok(payDemoWithdrawService.getDemoWithdrawPage(pageBo));
    }

    /**
     * 转账回调更新支付示例提现单状态。
     */
    @PostMapping("/update-transferred")
    @PermitAll
    public R<Boolean> updateDemoWithdrawTransferred(@RequestBody PayTransferNotifyReq notifyReq) {
        payDemoWithdrawService.updateDemoWithdrawTransferred(Long.valueOf(notifyReq.getMerchantTransferId()),
                notifyReq.getPayTransferId());
        return R.ok(Boolean.TRUE);
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

    /**
     * 支付转账通知请求。
     */
    @Data
    public static class PayTransferNotifyReq {
        private String merchantTransferId;
        private Long payTransferId;
    }

}
