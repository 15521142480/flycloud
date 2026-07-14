package com.fly.test.controller;

import com.fly.common.domain.model.R;
import com.fly.test.service.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 测试
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/seata")
public class SeataTestController {


    private final ITestService testService;



    /**
     * seata测试
     */
    @GetMapping("/seataTest/{isRollback}")
    public R<Void> seataTest(@NotNull(message = "isRollback（是否回滚）不能为空") @PathVariable Integer isRollback) {
        return R.result(testService.seataTest(isRollback));
    }



}
