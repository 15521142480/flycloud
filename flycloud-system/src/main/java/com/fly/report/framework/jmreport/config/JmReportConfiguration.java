package com.fly.report.framework.jmreport.config;

import com.fly.common.config.properties.AuthProperties;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.report.framework.jmreport.core.service.JmOnlDragExternalServiceImpl;
import com.fly.report.framework.jmreport.core.service.JmReportTokenServiceImpl;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 积木报表配置。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "org.jeecg.modules.jmreport")
public class JmReportConfiguration {

    @Bean
    public JmReportTokenServiceI jmReportTokenService(RedisUtils redisUtils, AuthProperties authProperties) {
        return new JmReportTokenServiceImpl(redisUtils, authProperties);
    }

    @Bean
    @Primary
    public JmOnlDragExternalServiceImpl jmOnlDragExternalService() {
        return new JmOnlDragExternalServiceImpl();
    }

}
