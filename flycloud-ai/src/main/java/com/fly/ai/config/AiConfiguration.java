package com.fly.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.http.HttpClient;

/**
 * 原生 HTTP 客户端与流式任务线程池配置。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfiguration {

    @Bean
    public HttpClient aiHttpClient(AiProperties properties) {
        return HttpClient.newBuilder()
                .connectTimeout(properties.getOpenai().getConnectTimeout())
                .build();
    }

    @Bean(name = "aiStreamTaskExecutor")
    public ThreadPoolTaskExecutor aiStreamTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ai-stream-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        return executor;
    }

}
