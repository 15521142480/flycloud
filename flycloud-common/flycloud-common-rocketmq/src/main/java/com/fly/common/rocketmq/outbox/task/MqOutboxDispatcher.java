package com.fly.common.rocketmq.outbox.task;

import com.fly.common.rocketmq.config.properties.RocketMqProperties;
import com.fly.common.rocketmq.outbox.domain.MqOutboxMessage;
import com.fly.common.rocketmq.producer.RocketMqProducer;
import com.fly.common.rocketmq.producer.RocketMqSendCallback;
import com.fly.common.rocketmq.outbox.service.MqOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/** 周期性投递本地消息表中的待发送消息。 */
@Slf4j
@RequiredArgsConstructor
public class MqOutboxDispatcher {

    private final RocketMqProperties properties;
    private final MqOutboxService outboxService;
    private final RocketMqProducer producer;

    /** 扫描待发送消息，并通过统一生产者异步投递到 RocketMQ。 */
    @Scheduled(fixedDelayString = "${flycloud.rocketmq.outbox.dispatch-delay-millis:5000}")
    public void dispatch() {
        if (!properties.getOutbox().isEnabled()) {
            return;
        }
        for (MqOutboxMessage outbox : outboxService.findPending(properties.getOutbox().getBatchSize())) {
            String dispatchToken = outboxService.claim(outbox.getId(), properties.getOutbox().getClaimLeaseSeconds());
            if (dispatchToken == null) {
                continue;
            }
            try {
                producer.sendAsync(outbox.getTopic(), outbox.getTag(), outboxService.readMessage(outbox), new RocketMqSendCallback() {
                    /** 将 Broker 成功确认的消息标记为已发送。 */
                    @Override
                    public void onSuccess() {
                        outboxService.markSent(outbox.getId(), dispatchToken);
                    }

                    /** 记录异步发送异常并让本地消息进入延迟重试。 */
                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("本地消息投递失败，messageId={}", outbox.getMessageId(), throwable);
                        outboxService.markFailure(outbox, dispatchToken, properties.getOutbox().getMaxRetryCount(), throwable);
                    }
                });
            } catch (Exception exception) {
                log.error("本地消息调度失败，messageId={}", outbox.getMessageId(), exception);
                outboxService.markFailure(outbox, dispatchToken, properties.getOutbox().getMaxRetryCount(), exception);
            }
        }
    }
}
