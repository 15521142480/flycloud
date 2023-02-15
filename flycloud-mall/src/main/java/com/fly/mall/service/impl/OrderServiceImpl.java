package com.fly.mall.service.impl;

import com.fly.mall.service.OrderService;
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
