package com.fly.system.controller;

import com.fly.common.model.R;
import com.fly.system.domain.User;
import com.fly.system.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试-控制层
 *
 * @author lxs
 * @date 2023/4/17
 */
@RestController
@RequestMapping("/sys/test")
@AllArgsConstructor
public class TestController {


    private final TestService testService;


    /**
     * 获取用户列表
     */
    @GetMapping("/getUserList")
    public R<List<User>> getUserList() {

        return R.ok(testService.getUserList());
    }


}
