package com.fly.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * rsa 配置。
 */
@Data
@ConfigurationProperties(prefix = "security.rsa.keys")
public class RsaProperties {


    /**
     * 登录密码
     */
    private KeyInfo flyCloudLoginPassword;


    /**
     * 支付密码
     */
    private KeyInfo flyCloudPayPassword;


    /**
     * 开放接口
     */
    private KeyInfo flyCloudOpenApi;



    // =========================================================================================================

    @Data
    public static class KeyInfo {

        /**
         * 公钥
         */
        private String publicKey;

        /**
         * 私钥
         */
        private String privateKey;
    }


}
