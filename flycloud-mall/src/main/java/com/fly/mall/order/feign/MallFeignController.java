package com.fly.mall.order.feign;

import com.fly.api.flycloud_mall.feign.MallFeignClient;
import com.fly.mall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城内部接口-控制层
 *
 * @author lxs
 * @date 2023/2/13
 */
@RestController
@RequiredArgsConstructor
public class MallFeignController implements MallFeignClient {

    private final OrderService orderService;


    @Override
    public String getOrderInfo(String orderId) {
        return orderService.getOrderInfo(orderId);
    }
}
