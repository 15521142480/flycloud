package com.fly.test.controller;

import com.fly.common.model.R;
import com.fly.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * log测试-控制层
 *
 * @author lxs
 * @date 2023/4/23
 */
@RestController
@RequestMapping("/test/log")
@Slf4j
public class LogTestController {


    @GetMapping("/print")
    public R<String> print() {

        System.out.println("===这是控制台日志");
        log.debug("===这是{}日志", "debug");
        log.info("===这是{}日志", "info");
        log.warn("===这是{}日志", "warn");
        log.error("===这是{}日志", "error");
        log.trace("===这是{}日志", "trace");
        return R.ok("set成功");
    }


}
