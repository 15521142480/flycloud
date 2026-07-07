package com.fly.system.api.system.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.system.api.constants.SystemFeignApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 角色权限远程调用接口
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysRoleApi")
public interface ISysRoleApi {



    /**
     * 根据ids验证角色
     *
     * @param roleIds
    */
    @GetMapping(SystemFeignApiConstants.PROVIDER_ROLE_VALID_IDS)
    R<Boolean> validateRoleByIds(@RequestParam("roleIds") Set<Long> roleIds);

}
