package com.fly.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务资源安全配置
 *
 * @author lxs
 * @date 2024/08/14
 */
@Component
@ConfigurationProperties(prefix = "gateway.server.security")
@Data
public class GatewayServerSecurityProperties {


    /**
     * 网关认证开关
     */
    private Boolean enable = false;

    /**
     * 白名单 (授权、swagger、监控中心等url)
     */
    private List<String> ignoreUrls = new ArrayList<>();



}
