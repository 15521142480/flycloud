package com.fly.system.api.pay.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 支付订单远程调用接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "PayOrderApi")
public interface IPayOrderApi {

    /**
     * 创建支付订单。
     */
    @PostMapping("/provider/sys/pay/order/create")
    R<Long> createPayOrder(@RequestBody PayOrderCreateReqDto createReqDto);

}
