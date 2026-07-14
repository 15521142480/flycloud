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
 * test
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ITestController implements ITestApi {


    private final ITestService testService;


    @Override
    @PostMapping(BpmApiPaths.PROVIDER_TEST_SEATA)
    public R<Integer> seataTest(@Valid @RequestBody SeataTestDataReqDTO reqDTO) {
        return R.ok(testService.seataTest(reqDTO.getDataSourceTwoTestData()));
    }

    @Override
    @GetMapping(BpmApiPaths.PROVIDER_TEST_SEATA + "/list")
    public R<List<SeataTestDataVo>> listSeataTestData() {
        return R.ok(testService.listSeataTestData());
    }
}
