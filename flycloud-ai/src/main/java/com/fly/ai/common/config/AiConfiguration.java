package com.fly.ai.common.config;

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

    /**
     * 创建调用模型原生接口的 JDK HTTP 客户端。
     *
     * @param properties AI 配置
     * @return HTTP 客户端
     */
    @Bean
    public HttpClient aiHttpClient(AiProperties properties) {
        return HttpClient.newBuilder()
                .connectTimeout(properties.getOpenai().getConnectTimeout())
                .build();
    }

    /**
     * 创建处理模型流式响应的专用线程池。
     *
     * @return 流式任务线程池
     */
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
