package com.fly.bpm.api.feign;

import com.fly.bpm.api.constants.BpmFeignApiConstants;
import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * d
 *
 * @author: lxs
 * @date: 2025/8/15
 */
@FeignClient(value = ServerNames.BPM_SERVER_NAME, contextId = "TestApi")
public interface ITestApi {


    /**
     * seata 测试
     */
    @PostMapping(BpmFeignApiConstants.PROVIDER_TEST_SEATA)
    R<Void> seataTest();


}
