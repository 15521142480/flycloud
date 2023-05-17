package com.fly.common.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全授权配置属性
 *
 * @author lxs
 * @date 2023/4/28
 */
@Component
@ConfigurationProperties(prefix = "security.oauth2.client")
@Data
public class SecurityAuthorizationProperties {

    /**
     * 客户端标识 ID
     */
    private String clientId;

    /**
     * 客户端安全码
     */
    private String clientSecret;

    /**
     * 授权类型
     */
    private String[] grantTypes;

    /**
     * token有效期
     */
    private int tokenValidityTime;

    /**
     * refresh-token有效期
     */
    private int refreshTokenValidityTime;

    /**
     * 客户端访问范围
     */
    private String[] scopes;

    /**
     * 白名单 (对外直接暴露服务资源的URL)
     */
    private List<String> ignoreUrls = new ArrayList<>();

}
