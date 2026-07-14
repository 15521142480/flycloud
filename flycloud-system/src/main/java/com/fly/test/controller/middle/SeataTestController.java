package com.fly.test.controller.middle;

import com.fly.common.domain.model.R;
import com.fly.test.domain.bo.SeataTestBo;
import com.fly.test.domain.vo.SeataTestDataListVo;
import com.fly.test.service.ITestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Seata 分布式事务测试。
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
     * 执行 Seata 分布式事务测试。
     *
     * @param isRollback 是否模拟异常并回滚：0-不回滚，1-回滚
     * @param bo 测试数据
     * @return 执行结果
     */
    @PostMapping("/seataTest/{isRollback}")
    public R<Boolean> seataTest(@NotNull(message = "isRollback（是否回滚）不能为空")
                                @Min(value = 0, message = "isRollback只允许为0或1")
                                @Max(value = 1, message = "isRollback只允许为0或1")
                                @PathVariable Integer isRollback,
                                @Valid @RequestBody SeataTestBo bo) {
        return R.result(testService.seataTest(isRollback, bo.getDataSourceOneTestData(), bo.getDataSourceTwoTestData()));
    }

    /**
     * 查询两个数据源各自最新的十条测试数据。
     *
     * @return 数据源一、数据源二的测试数据
     */
    @GetMapping("/testData/list")
    public R<SeataTestDataListVo> listTestData() {
        return R.ok(testService.listTestData());
    }

}
