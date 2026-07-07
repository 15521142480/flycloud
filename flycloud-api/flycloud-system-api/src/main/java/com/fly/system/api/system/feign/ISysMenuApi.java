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
 * 菜单权限远程调用接口
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysMenuApi")
public interface ISysMenuApi {


    /**
     * 根据角色Id查询菜单列表
     * @param roleId　角色ID
     * @return List
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_MENU_BY_ROLE_ID)
    R<List<String>> getListByRoleId(@RequestParam("roleId") String roleId);



}
