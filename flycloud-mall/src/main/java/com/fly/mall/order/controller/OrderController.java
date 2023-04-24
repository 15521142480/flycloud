package com.fly.mall.order.controller;

import com.fly.mall.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单模块
 *
 * @author lxs
 * @date 2023/2/12
 */
@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {


    private final OrderService orderService;


    /**
     * 获取订单信息
     */
    @GetMapping("/getOrderInfo")
    public String getOrderInfo(String orderId) {

        return "订单信息:" + orderService.getOrderInfo(orderId);
    }


}
