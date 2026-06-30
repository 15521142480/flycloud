package com.fly.im.framework.rtc.config;

import com.fly.im.framework.config.ImProperties;
import com.fly.im.framework.rtc.core.LiveKitClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * im 模块的 RTC 组件的 Configuration
 *
 * @author lxs
 * @date 2026-06-30
 */
@Configuration(proxyBeanMethods = false)
public class ImRtcConfiguration {

    /**
     * LiveKit 客户端
     */
    @Bean
    public LiveKitClient liveKitClient(ImProperties imProperties) {
        return new LiveKitClient(imProperties);
    }

}
