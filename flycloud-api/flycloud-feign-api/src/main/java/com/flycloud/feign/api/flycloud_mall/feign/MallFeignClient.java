package com.flycloud.feign.api.flycloud_mall.feign;

import com.fly.common.constant.ServerNames;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商城-接口服务
 *
 * @author lxs
 * @date 2023/2/13
 */
@FeignClient(name = ServerNames.MALL_SERVER_NAME)
public interface MallFeignClient {


    @GetMapping(value = "/feign/mall/getOrderInfo")
    String getOrderInfo(@RequestParam("orderId") String orderId);


}
