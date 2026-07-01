package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.pay.service.IPayDemoOrderService;
import com.fly.system.api.pay.domain.bo.PayDemoOrderCreateReqBo;
import com.fly.system.api.pay.domain.vo.PayDemoOrderVo;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付示例订单控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/demo-order")
public class PayDemoOrderController {

    private final IPayDemoOrderService payDemoOrderService;

    /**
     * 创建支付示例订单。
     */
    @PostMapping("/create")
    public R<Long> createDemoOrder(@Valid @RequestBody PayDemoOrderCreateReqBo createReqBo,
                                   HttpServletRequest request) {
        return R.ok(payDemoOrderService.createDemoOrder(UserUtils.getCurUserId(), createReqBo, getClientIp(request)));
    }

    /**
     * 分页查询支付示例订单。
     */
    @GetMapping("/page")
    public R<PageVo<PayDemoOrderVo>> getDemoOrderPage(PageBo pageBo) {
        return R.ok(payDemoOrderService.getDemoOrderPage(pageBo));
    }

    /**
     * 支付回调更新示例订单为已支付。
     */
    @PostMapping("/update-paid")
    @PermitAll
    public R<Boolean> updateDemoOrderPaid(@RequestBody PayOrderNotifyReq notifyReq) {
        payDemoOrderService.updateDemoOrderPaid(Long.valueOf(notifyReq.getMerchantOrderId()), notifyReq.getPayOrderId());
        return R.ok(Boolean.TRUE);
    }

    /**
     * 发起支付示例订单退款。
     */
    @PutMapping("/refund")
    public R<Boolean> refundDemoOrder(@RequestParam("id") Long id, HttpServletRequest request) {
        payDemoOrderService.refundDemoOrder(id, getClientIp(request));
        return R.ok(Boolean.TRUE);
    }

    /**
     * 退款回调更新示例订单为已退款。
     */
    @PostMapping("/update-refunded")
    @PermitAll
    public R<Boolean> updateDemoOrderRefunded(@RequestBody PayRefundNotifyReq notifyReq) {
        payDemoOrderService.updateDemoOrderRefunded(Long.valueOf(notifyReq.getMerchantOrderId()),
                notifyReq.getMerchantRefundId(), notifyReq.getPayRefundId());
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
     * 支付订单通知请求。
     */
    @Data
    public static class PayOrderNotifyReq {
        private String merchantOrderId;
        private Long payOrderId;
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

}
