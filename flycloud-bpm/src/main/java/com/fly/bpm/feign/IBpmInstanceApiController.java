package com.fly.bpm.feign;

import com.fly.bpm.api.constants.BpmFeignApiConstants;
import com.fly.bpm.api.domain.dto.instance.BpmProcessInstanceCreateReqDTO;
import com.fly.bpm.api.feign.IBpmInstanceApi;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.domain.model.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 流程实例api - 控制层
 *
 * @author: lxs
 * @date: 2024/12/18
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class IBpmInstanceApiController implements IBpmInstanceApi {


    private final BpmInstanceService bpmInstanceService;



    /**
     * 创建流程实例，返回实例编号
     *
     * @param userId 用户id
     * @param createReqDTO 创建流程实例参数
     */
    @Override
    @PostMapping(BpmFeignApiConstants.PROVIDER_INSTANCE_CREATE)
    public R<String> createInstance(@RequestParam("userId") Long userId, @Valid @RequestBody BpmProcessInstanceCreateReqDTO createReqDTO) {
        return R.ok(bpmInstanceService.createProcessInstance(userId, createReqDTO));
    }

}
