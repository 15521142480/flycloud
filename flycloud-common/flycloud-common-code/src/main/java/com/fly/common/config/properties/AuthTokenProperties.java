package com.fly.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 登录 Token 配置。
 */
@Data
@ConfigurationProperties(prefix = "security.auth.token")
public class AuthTokenProperties {

    /**
     * 登录态有效期，单位秒。接口请求认证成功后会滑动续期。
     */
    private long loginTimeoutSeconds = 7200;

    /**
     * 刷新令牌有效期，单位秒。
     */
    private long refreshTokenTimeoutSeconds = 30L * 24 * 60 * 60;

    public Duration getLoginTimeout() {
        return Duration.ofSeconds(loginTimeoutSeconds);
    }

    public Duration getRefreshTokenTimeout() {
        return Duration.ofSeconds(refreshTokenTimeoutSeconds);
    }
}
