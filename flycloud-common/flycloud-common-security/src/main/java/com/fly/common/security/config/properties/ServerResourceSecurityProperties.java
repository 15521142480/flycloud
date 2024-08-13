package com.fly.common.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务资源安全配置
 *
 * @author lxs
 * @date 2023/4/28
 */
@Component
@ConfigurationProperties(prefix = "server.resource.security")
@Data
public class ServerResourceSecurityProperties {

    /**
     * 客户端标识 ID
     */
    private String clientId;

    /**
     * 客户端安全码
     */
    private String clientSecret;

    /**
     * 客户端访问范围
     */
    private String[] scopes;

    /**
     * 白名单 (对外直接暴露服务资源的URL)
     */
    private List<String> ignoreUrls = new ArrayList<>();

}
