package com.fly.api.flycloud_system.feign;

import com.fly.api.flycloud_system.domain.UserAuthInfo;
import com.fly.api.flycloud_system.feign.fallback.SysUserFeignFallbackClient;
import com.fly.common.constant.ServerNames;
import com.fly.common.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 系统用户-feign客户端
 *
 * todo 依靠负载均衡策略做分布式的服务调度; 本项目使用的是loadbalancer
 *
 * @author lxs
 * @date 2023/5/2
 */
@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME, contextId = "sysUser", fallback = SysUserFeignFallbackClient.class)
public interface SysUserFeignClient {



    /**
     * 根据系统账号获取系统用户信息
     */
    @GetMapping(value = "/feign/system/getUserInfoByName")
    R<UserAuthInfo> getUserInfoByName(@RequestParam("username") String username);



}
