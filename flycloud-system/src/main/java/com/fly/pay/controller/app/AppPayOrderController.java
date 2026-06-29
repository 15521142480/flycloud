package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.pay.service.IPayOrderService;
import com.fly.system.api.pay.domain.bo.AppPayOrderSubmitReqVo;
import com.fly.system.api.pay.domain.vo.AppPayOrderSubmitRespVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 移动端 - 支付订单控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/pay/order")
public class AppPayOrderController {

    private final IPayOrderService payOrderService;

    /**
     * 获得支付订单。
     */
    @GetMapping("/get")
    public R<PayOrderRespVo> getOrder(@RequestParam(value = "id", required = false) Long id,
                                      @RequestParam(value = "no", required = false) String no,
                                      @RequestParam(value = "sync", required = false) Boolean sync) {
        PayOrderRespVo order = no != null ? payOrderService.getOrder(no) : payOrderService.getOrder(id);
        if (order == null) {
            return R.ok((PayOrderRespVo) null);
        }
        Long userId = UserUtils.getCurUserId();
        if (order.getUserId() != null && !Objects.equals(order.getUserId(), userId)) {
            return R.ok((PayOrderRespVo) null);
        }
        return R.ok(order);
    }

    /**
     * 提交支付订单。
     */
    @PostMapping("/submit")
    public R<AppPayOrderSubmitRespVo> submitPayOrder(@RequestBody AppPayOrderSubmitReqVo submitReqVo,
                                                     HttpServletRequest request) {
        return R.ok(payOrderService.submitOrder(UserUtils.getCurUserId(), submitReqVo, getClientIp(request)));
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
