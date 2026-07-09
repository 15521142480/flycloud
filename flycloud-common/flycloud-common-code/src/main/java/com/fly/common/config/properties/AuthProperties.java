package com.fly.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录 Token 配置。
 */
@Data
@ConfigurationProperties(prefix = "security.auth")
public class AuthProperties {


    /**
     * token配置
     */
    private Token token;

    /**
     * 白名单 (对外直接暴露服务资源的URL)
     */
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 是否启用网关验证（是：接口都拦截验证；否：全部接口在网关服务都为白名单）
     */
    private boolean gatewayEnable = true;

    /**
     * Feign内部接口签名配置。
     */
    private Feign feign = new Feign();

//    private String clientId;
//    private String clientSecret;
//    private String[] scopes;


    // =========================================================================================================

    @Data
    public static class Token {

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

    @Data
    public static class Feign {

        /**
         * 是否启用Feign内部接口签名验证。
         */
        private boolean enabled = true;

        /**
         * Feign内部接口签名密钥，各个服务必须保持一致。
         */
        private String secret = "flycloud-feign-secret";

        /**
         * Feign签名时间戳有效期，单位秒。
         */
        private long expireSeconds = 300;
    }


}
