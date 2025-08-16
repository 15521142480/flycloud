package com.fly.test.controller;

import com.fly.common.domain.model.R;
import com.fly.test.service.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 测试
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {


    private final ITestService testService;



    @GetMapping("/seataTest/{isRollback}")
    public R<Void> seataTest(@NotNull(message = "isRollback（是否回滚）不能为空") @PathVariable Integer isRollback) {
        return R.ok(testService.seataTest(isRollback));
    }

}
