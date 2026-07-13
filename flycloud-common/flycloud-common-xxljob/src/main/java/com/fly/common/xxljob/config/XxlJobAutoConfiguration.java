package com.fly.common.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

/**
 * XXL-JOB 执行器自动配置。
 *
 * <p>业务服务引入公共模块并显式启用执行器后，自动向调度中心注册。</p>
 *
 * @author lxs
 * @date 2026-07-13
 */
@AutoConfiguration
@ConditionalOnClass(XxlJobSpringExecutor.class)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.job.executor", name = "enabled", havingValue = "true")
public class XxlJobAutoConfiguration {

    /**
     * 创建 XXL-JOB Spring 执行器。
     */
    @Bean
    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties properties) {
        XxlJobProperties.Admin admin = properties.getAdmin();
        XxlJobProperties.Executor executor = properties.getExecutor();
        Assert.hasText(admin.getAddresses(), "xxl.job.admin.addresses must not be blank");
        Assert.hasText(admin.getAccessToken(), "xxl.job.admin.access-token must not be blank");
        Assert.hasText(executor.getAppname(), "xxl.job.executor.appname must not be blank");
        Assert.isTrue(executor.getPort() > 0, "xxl.job.executor.port must be greater than 0");

        XxlJobSpringExecutor xxlJobExecutor = new XxlJobSpringExecutor();
        xxlJobExecutor.setAdminAddresses(admin.getAddresses());
        xxlJobExecutor.setAccessToken(admin.getAccessToken());
        xxlJobExecutor.setTimeout(admin.getTimeout());
        xxlJobExecutor.setAppname(executor.getAppname());
        xxlJobExecutor.setAddress(executor.getAddress());
        xxlJobExecutor.setIp(executor.getIp());
        xxlJobExecutor.setPort(executor.getPort());
        xxlJobExecutor.setLogPath(executor.getLogPath());
        xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        return xxlJobExecutor;
    }
}
