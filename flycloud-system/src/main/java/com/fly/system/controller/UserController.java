package com.fly.system.controller;

import com.fly.api.flycloud_mall.feign.MallFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户模块
 *
 * @author lxs
 * @date 2023/2/12
 */
@RestController
@RequestMapping("/sys/user")
//@AllArgsConstructor
public class UserController {


//    private final MallFeignClient mallFeignClient;

    @Resource
    private MallFeignClient mallFeignClient;

//    @Value("${sys.name}")
    private String sysName;

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo")
    public String getUserInfo(String userId) {

        System.out.println("读取系统名称:" + sysName);

        String msg = "我是系统用户信息:{用户id:"+userId+",名字:小赖}";
        System.out.println(msg);

        System.out.println("正在获取商城系统的订单信息...");
        String orderInfo = mallFeignClient.getOrderInfo("123456789");
        System.out.println("成功获取商城系统的订单信息:" + orderInfo);

        return msg + ";获取商城系统的订单信息:" + orderInfo;
    }


}
