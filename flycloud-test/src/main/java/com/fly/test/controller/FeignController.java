package com.fly.test.controller;

import com.fly.common.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign 服务调用 - 控制层
 *
 * @author lxs
 * @date 2023/5/2
 */
@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
public class FeignController {


//    private final SysUserFeignClient sysUserFeignClient;


    /**
     * 根据系统账号获取系统用户信息
     *
     * @param username
     *
    */
//    @GetMapping("/getUserInfoByName")
//    public R<UserAuthInfo> getUserInfoByName(String username) {
//
//        return sysUserFeignClient.getUserInfoByName(username);
//    }



}
