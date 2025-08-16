package com.fly.bpm.feign;

import com.fly.bpm.api.constants.BpmFeignApiConstants;
import com.fly.bpm.api.feign.ITestApi;
import com.fly.bpm.test.service.ITestService;
import com.fly.common.domain.model.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(BpmFeignApiConstants.PROVIDER_TEST_SEATA)
    public R<Void> seataTest() {
        return R.ok(testService.seataTest());
    }
}
