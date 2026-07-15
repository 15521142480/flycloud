package com.fly.common.rocketmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.common.rocketmq.mapper.MqConsumeRecordMapper;
import com.fly.common.rocketmq.mapper.MqOutboxMessageMapper;
import com.fly.common.rocketmq.producer.DefaultRocketMqProducer;
import com.fly.common.rocketmq.producer.RocketMqProducer;
import com.fly.common.rocketmq.service.MqConsumeIdempotentService;
import com.fly.common.rocketmq.service.MqOutboxService;
import com.fly.common.rocketmq.task.MqOutboxDispatcher;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/** RocketMQ 公共自动配置。 */
@AutoConfiguration(after = RocketMQAutoConfiguration.class)
@EnableScheduling
@EnableConfigurationProperties(RocketMqProperties.class)
@ConditionalOnProperty(prefix = "flycloud.rocketmq", name = "enabled", havingValue = "true")
public class RocketMqAutoConfiguration {

    /** 注册唯一允许业务依赖的 RocketMQ 生产者封装。 */
    @Bean
    @ConditionalOnMissingBean
    public RocketMqProducer rocketMqProducer(RocketMQTemplate template) {
        return new DefaultRocketMqProducer(template);
    }

    /** 注册本地消息表读写服务。 */
    @Bean
    @ConditionalOnMissingBean
    public MqOutboxService mqOutboxService(MqOutboxMessageMapper mapper, ObjectMapper objectMapper) {
        return new MqOutboxService(mapper, objectMapper);
    }

    /** 注册消费成功记录的幂等服务。 */
    @Bean
    @ConditionalOnMissingBean
    public MqConsumeIdempotentService mqConsumeIdempotentService(MqConsumeRecordMapper mapper) {
        return new MqConsumeIdempotentService(mapper);
    }

    /** 注册异步投递本地消息表的定时调度器。 */
    @Bean
    @ConditionalOnMissingBean
    public MqOutboxDispatcher mqOutboxDispatcher(RocketMqProperties properties, MqOutboxService service,
                                                  RocketMqProducer producer) {
        return new MqOutboxDispatcher(properties, service, producer);
    }
}
