package com.flycloud.system.api.feign;

import com.fly.common.constant.ProviderConstants;
import com.fly.common.constant.ServerNames;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色权限远程调用接口
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME)
public interface ISysRolePermissionProvider {


    /**
     * 通过roleId获取权限列表
     * @param roleId　角色ID
     * @return List
     */
    @GetMapping(ProviderConstants.PROVIDER_ROLE_PERMISSION)
    List<String> getMenuIdByRoleId(@RequestParam("roleId") String roleId);
}
