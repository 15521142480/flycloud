package com.fly.member.search.consumer;

import com.fly.common.enums.user.UserTypeEnum;
import com.fly.common.rocketmq.codec.MqMessageCodec;
import com.fly.common.rocketmq.constant.RocketMqConsumerGroupConstants;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.consumer.AbstractIdempotentRocketMqConsumer;
import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import com.fly.common.rocketmq.idempotent.service.MqConsumeIdempotentService;
import com.fly.common.websocket.sender.WebSocketMessageSender;
import com.fly.member.search.index.IndexOperationNotificationEvent;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 向拥有 yunwei 角色的在线后台用户推送会员索引操作成功通知。
 */
@Component
@ConditionalOnProperty(prefix = "flycloud.rocketmq", name = "enabled", havingValue = "true")
@RocketMQMessageListener(
        topic = RocketMqTopicConstants.SYSTEM_USER_EVENT,
        selectorExpression = RocketMqTagConstants.NOTIFY,
        consumerGroup = RocketMqConsumerGroupConstants.SYSTEM_OPERATION_NOTIFY,
        consumeMode = ConsumeMode.CONCURRENTLY,
        maxReconsumeTimes = 16)
public class IndexOperationNotificationConsumer extends AbstractIdempotentRocketMqConsumer<IndexOperationNotificationEvent> {

    /** 接收索引操作通知的后台角色标识。 */
    public static final String NOTIFICATION_ROLE_CODE = "yunwei";

    private final WebSocketMessageSender webSocketMessageSender;

    /**
     * 创建会员索引操作通知消费者。
     *
     * @param idempotentService 消费幂等服务
     * @param messageCodec 消息编解码器
     * @param webSocketMessageSender 后台 WebSocket 消息发送器
     */
    public IndexOperationNotificationConsumer(MqConsumeIdempotentService idempotentService, MqMessageCodec messageCodec,
                                              WebSocketMessageSender webSocketMessageSender) {
        super(idempotentService, messageCodec, IndexOperationNotificationEvent.class);
        this.webSocketMessageSender = webSocketMessageSender;
    }

    /**
     * @return 运维通知消费者组。
     */
    @Override
    protected String consumerGroup() {
        return RocketMqConsumerGroupConstants.SYSTEM_OPERATION_NOTIFY;
    }

    /**
     * @return 用户领域事件 Topic。
     */
    @Override
    protected String topic() {
        return RocketMqTopicConstants.SYSTEM_USER_EVENT;
    }

    /**
     * 向目标运维用户逐一推送索引操作完成通知。
     */
    @Override
    protected void handle(MqMessage<IndexOperationNotificationEvent> message) {
        IndexOperationNotificationEvent event = message.getPayload();
        if (event == null || event.getUserIds() == null) {
            return;
        }
        event.getUserIds().forEach(userId -> webSocketMessageSender.send(
                UserTypeEnum.ADMIN.getValue(), userId, "member-user-index-operation", event.getContent()));
    }
}
