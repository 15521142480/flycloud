package com.fly.pay.feign;

import com.fly.common.domain.model.R;
import com.fly.pay.service.IPayOrderService;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import com.fly.system.api.pay.feign.IPayOrderApi;
import com.fly.system.api.pay.path.PayApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统内部接口 - 支付订单控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RestController
@RequiredArgsConstructor
public class PayOrderApiController implements IPayOrderApi {

    private final IPayOrderService payOrderService;

    /**
     * 创建支付订单。
     */
    @Override
    @PostMapping(PayApiPaths.PROVIDER_ORDER_CREATE)
    public R<Long> createPayOrder(PayOrderCreateReqDto createReqDto) {
        return R.ok(payOrderService.createPayOrder(createReqDto));
    }

    /**
     * 查询支付订单。
     */
    @Override
    @GetMapping(PayApiPaths.PROVIDER_ORDER_GET)
    public R<PayOrderRespVo> getOrder(Long id) {
        return R.ok(payOrderService.getOrder(id));
    }

}
