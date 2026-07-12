package com.fly.common.report.jmreport.config;

import com.fly.common.config.properties.AuthProperties;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.report.jmreport.core.service.JmOnlDragExternalServiceImpl;
import com.fly.common.report.jmreport.core.service.JmReportTokenServiceImpl;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

/**
 * 积木报表自动配置。
 *
 * <p>积木报表只是 {@code flycloud-common-report} 支持的一种实现；其它报表框架应使用独立包和独立自动配置类。</p>
 *
 * @author lxs
 * @date 2026-07-12
 */
@AutoConfiguration
@ComponentScan(basePackages = "org.jeecg.modules.jmreport")
public class JmReportAutoConfiguration {

    /**
     * 提供积木报表所需的登录令牌校验与权限适配。
     */
    @Bean
    public JmReportTokenServiceI jmReportTokenService(RedisUtils redisUtils, AuthProperties authProperties) {
        return new JmReportTokenServiceImpl(redisUtils, authProperties);
    }

    /**
     * 提供积木仪表盘扩展能力。
     */
    @Bean
    @Primary
    public JmOnlDragExternalServiceImpl jmOnlDragExternalService() {
        return new JmOnlDragExternalServiceImpl();
    }

}
