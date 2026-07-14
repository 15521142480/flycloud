package com.fly.test.controller.middle;

import com.fly.common.domain.model.R;
import com.fly.test.service.ITestService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * rocketmq测试
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/rocketmq")
public class RocketmqTestController {


    private final ITestService testService;



    /**
     * 测试
     */
    @GetMapping("/test/{type}/{indexName}")
    public R<Void> seataTest(@NotNull(message = "type不能为空(1到4分别是增删改查)") @PathVariable Integer type, @NotNull(message = "indexName不能为空") @PathVariable String indexName) throws IOException {
        return R.result(testService.esTest(type, indexName));
    }

}
