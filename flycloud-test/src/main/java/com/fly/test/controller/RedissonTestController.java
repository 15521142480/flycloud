package com.fly.test.controller;

import com.fly.common.model.R;
import com.fly.common.redis.utils.RedisUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redisson测试
 *
 * @author lxs
 * @date 2023/4/23
 */
@RestController
@RequestMapping("/test/redisson")
public class RedissonTestController {



    @GetMapping("/set")
    public R<String> redisSet(String key, String value) {

        System.out.println("redis设值开始...");
        RedisUtils.setCacheObject(key, value);
        System.out.println("redis设值结束...");
        return R.ok("set成功");
    }




}
