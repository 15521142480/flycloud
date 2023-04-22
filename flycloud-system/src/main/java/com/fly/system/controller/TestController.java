package com.fly.system.controller;

import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.model.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


//    private final TestService testService;
//
//    /**
//     * 获取用户分页列表
//     */
//    @GetMapping("/getUserPageList")
//    public R<PageVo<UserVo>> getUserPageList(UserBo userDto) {
//
//        return R.ok(testService.getUserPageList(userDto));
//    }
//
//
//    /**
//     * 获取用户列表
//     */
//    @GetMapping("/getUserList")
//    public R<List<UserVo>> getUserList() {
//
//        return R.ok(testService.getUserList());
//    }
//
//
//    /**
//     * 获取用户信息
//     */
//    @GetMapping("/getUserInfo")
//    public R<UserVo> getUserInfo(@RequestParam(required = true) int uuid) {
//
//        return R.ok(testService.getUserInfo(uuid));
//    }

}
