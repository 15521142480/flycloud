package com.fly.bpm.api.feign;

import com.fly.bpm.api.path.BpmApiPaths;
import com.fly.bpm.api.domain.dto.instance.BpmProcessInstanceCreateReqDTO;
import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

/**
 * 流程实例api
 *
 * 对外系统开发自定义表单流程的提交操作
 *
 * @author: lxs
 * @date: 2025/12/18
 */
@FeignClient(value = ServerNames.BPM_SERVER_NAME, contextId = "BpmInstanceApi")
public interface IBpmInstanceApi {


    /**
     * 创建流程实例，返回实例编号
     *
     * @param userId 用户id
     * @param createReqDTO 创建流程实例参数
     */
    @PostMapping(BpmApiPaths.PROVIDER_INSTANCE_CREATE)
    R<String> createInstance(@RequestParam("userId") Long userId, @Valid @RequestBody BpmProcessInstanceCreateReqDTO createReqDTO);


}
