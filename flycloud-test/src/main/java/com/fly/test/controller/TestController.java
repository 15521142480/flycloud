package com.fly.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


    /**
     * 测试
     *
     */
    @GetMapping("/test")
    public String test(String userId) {

        System.out.println("Hello,这是测试");
        return "Hello,这是测试";
    }


    /**
     * post测试
     *
     */
    @PostMapping("/testPost")
    public String testPost(String userId) {

        System.out.println("Hello,这是测试Post");
        return "Hello,这是测试Post";
    }


    public static void main(String[] args) {

        String str1 = "23";
        String str2 = "23";
        System.out.println("str:" + (str1 == str2));
        System.out.println("str:" + str1.equals(str2));
    }



}