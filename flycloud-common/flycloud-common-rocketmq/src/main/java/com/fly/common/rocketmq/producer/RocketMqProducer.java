package com.fly.common.rocketmq.producer;

import com.fly.common.rocketmq.domain.MqMessage;

/** 业务侧唯一允许依赖的消息生产者接口，禁止直接使用 RocketMQTemplate。 */
public interface RocketMqProducer {

    /**
     * 异步发送统一消息信封。
     *
     * @param topic 业务域 Topic 常量
     * @param tag 事件 Tag 常量
     * @param message 标准业务消息信封
     * @param callback Broker 确认或异常回调
     */
    void sendAsync(String topic, String tag, MqMessage<?> message, RocketMqSendCallback callback);
}
