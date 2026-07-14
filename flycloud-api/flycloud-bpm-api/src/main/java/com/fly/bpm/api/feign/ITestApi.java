package com.fly.bpm.api.feign;

import com.fly.bpm.api.domain.dto.SeataTestDataReqDTO;
import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import com.fly.bpm.api.path.BpmApiPaths;
import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
    @PostMapping(BpmApiPaths.PROVIDER_TEST_SEATA)
    R<Integer> seataTest(@Valid @RequestBody SeataTestDataReqDTO reqDTO);

    /**
     * 查询 BPM 数据源最新的十条 Seata 测试数据。
     */
    @GetMapping(BpmApiPaths.PROVIDER_TEST_SEATA + "/list")
    R<List<SeataTestDataVo>> listSeataTestData();


}
