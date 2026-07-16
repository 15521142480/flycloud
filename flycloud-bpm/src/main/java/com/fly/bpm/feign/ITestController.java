package com.fly.bpm.feign;

import com.fly.bpm.api.domain.dto.SeataTestDataReqDTO;
import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import com.fly.bpm.api.path.BpmApiPaths;
import com.fly.bpm.api.feign.ITestApi;
import com.fly.bpm.test.service.ITestService;
import com.fly.common.domain.model.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Seata 测试场景的数据源二 Feign 服务实现。
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ITestController implements ITestApi {

    private final ITestService testService;

    /**
     * 在数据源二写入 Seata 测试数据。
     *
     * @param reqDTO 数据源二测试数据请求
     * @return 影响行数
     */
    @Override
    @PostMapping(BpmApiPaths.PROVIDER_TEST_SEATA)
    public R<Integer> seataTest(@Valid @RequestBody SeataTestDataReqDTO reqDTO) {
        return R.ok(testService.seataTest(reqDTO.getDataSourceTwoTestData()));
    }

    /**
     * 查询数据源二最新的 Seata 测试数据。
     *
     * @return 测试数据列表
     */
    @Override
    @GetMapping(BpmApiPaths.PROVIDER_TEST_SEATA + "/list")
    public R<List<SeataTestDataVo>> listSeataTestData() {
        return R.ok(testService.listSeataTestData());
    }
}
