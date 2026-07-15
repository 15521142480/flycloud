package com.fly.common.rocketmq.producer;

import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;

/** RocketMQTemplate 的唯一公共封装出口。 */
@Slf4j
@RequiredArgsConstructor
public class DefaultRocketMqProducer implements RocketMqProducer {

    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 通过 RocketMQTemplate 异步发送统一消息信封。
     *
     * <p>业务层只能依赖 {@link RocketMqProducer}，不应直接使用底层 Template。</p>
     */
    @Override
    public void sendAsync(String topic, String tag, MqMessage<?> message, RocketMqSendCallback callback) {
        String destination = topic + ":" + tag;
        rocketMQTemplate.asyncSend(destination, MessageBuilder.withPayload(message).build(), new SendCallback() {
            /** 记录 Broker 确认结果并通知本地消息调度器。 */
            @Override
            public void onSuccess(SendResult sendResult) {
                log.debug("RocketMQ 已投递，messageId={}, destination={}, msgId={}",
                        message.getMessageId(), destination, sendResult.getMsgId());
                callback.onSuccess();
            }

            /** 记录异步投递异常并通知本地消息调度器进入重试。 */
            @Override
            public void onException(Throwable throwable) {
                log.error("RocketMQ 异步投递失败，messageId={}, destination={}",
                        message.getMessageId(), destination, throwable);
                callback.onFailure(throwable);
            }
        });
    }
}
