package com.fly.mall.order.service.impl;

import com.fly.mall.order.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * 订单-业务层
 *
 * @author lxs
 * @date 2023/2/13
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Override
    public String getOrderInfo(String orderId) {
        return "{订单id:"+orderId+",订单商品:苹果电脑}";
    }
}
