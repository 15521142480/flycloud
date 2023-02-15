package com.fly.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试-控制层
 *
 * @author lxs
 * @date 2023/2/15
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/test")
    public String getUserInfo(String userId) {

        System.out.println("Hello,这是测试");
        return "Hello,这是测试";
    }



}