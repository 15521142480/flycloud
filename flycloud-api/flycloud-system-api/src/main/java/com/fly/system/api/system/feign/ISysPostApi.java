package com.fly.system.api.system.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.system.api.constants.SystemFeignApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * 岗位远程调用接口
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysPostApi")
public interface ISysPostApi {


    /**
     * 根据ids验证岗位
     * @param ids　
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_POST_VALID_IDS)
    R<Boolean> validatePostByIds(@RequestParam("ids") Collection<Long> ids);
}
