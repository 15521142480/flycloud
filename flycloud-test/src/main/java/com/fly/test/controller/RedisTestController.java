package com.fly.test.controller;

import com.fly.common.domain.model.R;
import com.fly.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis测试
 *
 * @author lxs
 * @date 2023/4/23
 */
@RestController
@RequestMapping("/test/redis")
@RequiredArgsConstructor
public class RedisTestController {

    private final RedisUtils redisUtils;


    /**
     * redis设值
     */
    @GetMapping("/set")
    public R redisSet(String key, String value) {

        System.out.println("redis设值开始...");
//        RedisUtils.setCacheObject(key, value);
        System.out.println("redis设值结束...");
        return R.ok("set成功");
    }


    /**
     * redis拿值
     */
    @GetMapping("/get")
    public R redisGet(String key) {

        System.out.println("redis拿值开始...");
//        String value = RedisUtils.getCacheObject(key);
        System.out.println("redis拿值结束...");
        return R.ok("get成功,值为:");
    }


    /**
     * redis删值
     */
    @GetMapping("/del")
    public R delGet(String key) {

        System.out.println("redis删值开始...");
//        RedisUtils.deleteObject(key);
        System.out.println("redis删值结束...");
        return R.ok("del成功");
    }

}
