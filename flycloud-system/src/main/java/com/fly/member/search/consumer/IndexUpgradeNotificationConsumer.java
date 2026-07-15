package com.fly.member.search.consumer;

import com.fly.common.enums.user.UserTypeEnum;
import com.fly.common.rocketmq.constant.RocketMqConsumerGroupConstants;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.consumer.AbstractIdempotentRocketMqConsumer;
import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import com.fly.common.rocketmq.idempotent.service.MqConsumeIdempotentService;
import com.fly.common.websocket.sender.WebSocketMessageSender;
import com.fly.member.search.index.IndexUpgradeNotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** 向拥有 yunwei 角色的在线后台用户推送索引升级通知。 */
@Component
@ConditionalOnProperty(prefix = "flycloud.rocketmq", name = "enabled", havingValue = "true")
@RocketMQMessageListener(
        topic = RocketMqTopicConstants.SYSTEM_USER_EVENT,
        selectorExpression = RocketMqTagConstants.NOTIFY,
        consumerGroup = RocketMqConsumerGroupConstants.SYSTEM_OPERATION_NOTIFY,
        consumeMode = ConsumeMode.CONCURRENTLY,
        maxReconsumeTimes = 16)
public class IndexUpgradeNotificationConsumer extends AbstractIdempotentRocketMqConsumer<IndexUpgradeNotificationEvent> {

    private final WebSocketMessageSender webSocketMessageSender;

    /**
     * 创建索引升级通知消费者。
     *
     * @param idempotentService 消费幂等服务
     * @param objectMapper 消息 JSON 转换器
     * @param webSocketMessageSender 后台 WebSocket 消息发送器
     */
    public IndexUpgradeNotificationConsumer(MqConsumeIdempotentService idempotentService, ObjectMapper objectMapper,
                                            WebSocketMessageSender webSocketMessageSender) {
        super(idempotentService, objectMapper, IndexUpgradeNotificationEvent.class);
        this.webSocketMessageSender = webSocketMessageSender;
    }

    /** @return 运维通知消费者组。 */
    @Override
    protected String consumerGroup() {
        return RocketMqConsumerGroupConstants.SYSTEM_OPERATION_NOTIFY;
    }

    /** @return 用户领域事件 Topic。 */
    @Override
    protected String topic() {
        return RocketMqTopicConstants.SYSTEM_USER_EVENT;
    }

    /** @return 仅监听通知事件。 */
    @Override
    protected String tag() {
        return RocketMqTagConstants.NOTIFY;
    }

    /** 向目标运维用户逐一推送升级完成通知。 */
    @Override
    protected void handle(MqMessage<IndexUpgradeNotificationEvent> message) {
        IndexUpgradeNotificationEvent event = message.getPayload();
        if (event == null || event.getUserIds() == null) {
            return;
        }
        event.getUserIds().forEach(userId -> webSocketMessageSender.send(
                UserTypeEnum.ADMIN.getValue(), userId, "member-user-index-upgraded", event.getContent()));
    }
}
