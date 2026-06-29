package com.fly.pay.feign;

import com.fly.common.domain.model.R;
import com.fly.pay.service.IPayOrderService;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.feign.IPayOrderApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统内部接口 - 支付订单控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RestController
@RequiredArgsConstructor
public class PayOrderApiController implements IPayOrderApi {

    private final IPayOrderService payOrderService;

    /**
     * 创建支付订单。
     */
    @Override
    @PostMapping("/provider/sys/pay/order/create")
    public R<Long> createPayOrder(@RequestBody PayOrderCreateReqDto createReqDto) {
        return R.ok(payOrderService.createPayOrder(createReqDto));
    }

}
