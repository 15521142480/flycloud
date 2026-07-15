package com.fly.member.search.consumer;

import com.fly.common.rocketmq.constant.RocketMqConsumerGroupConstants;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.consumer.AbstractIdempotentRocketMqConsumer;
import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import com.fly.common.rocketmq.idempotent.service.MqConsumeIdempotentService;
import com.fly.member.search.index.MemberUserIndexEvent;
import com.fly.member.search.service.MemberUserSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/** 消费会员用户更新消息，按业务主键回查 MySQL 并更新 ES 投影。 */
@Slf4j
@Component
@ConditionalOnExpression("'${flycloud.elasticsearch.enabled:false}' == 'true' and '${flycloud.rocketmq.enabled:false}' == 'true'")
@RocketMQMessageListener(
        topic = RocketMqTopicConstants.SYSTEM_USER_EVENT,
        selectorExpression = RocketMqTagConstants.UPDATE,
        consumerGroup = RocketMqConsumerGroupConstants.SYSTEM_MEMBER_USER_ES_INDEX,
        consumeMode = ConsumeMode.CONCURRENTLY,
        maxReconsumeTimes = 16)
public class MemberUserIndexConsumer extends AbstractIdempotentRocketMqConsumer<MemberUserIndexEvent> {

    private final MemberUserSearchService memberUserSearchService;

    /**
     * 创建会员 ES 投影消费者。
     *
     * @param idempotentService 消费幂等服务
     * @param objectMapper 消息 JSON 转换器
     * @param memberUserSearchService 会员搜索业务编排服务
     */
    public MemberUserIndexConsumer(MqConsumeIdempotentService idempotentService, ObjectMapper objectMapper,
                                   MemberUserSearchService memberUserSearchService) {
        super(idempotentService, objectMapper, MemberUserIndexEvent.class);
        this.memberUserSearchService = memberUserSearchService;
    }

    /** @return 会员 ES 投影消费者组。 */
    @Override
    protected String consumerGroup() {
        return RocketMqConsumerGroupConstants.SYSTEM_MEMBER_USER_ES_INDEX;
    }

    /** @return 用户领域事件 Topic。 */
    @Override
    protected String topic() {
        return RocketMqTopicConstants.SYSTEM_USER_EVENT;
    }

    /** @return 仅监听用户更新事件。 */
    @Override
    protected String tag() {
        return RocketMqTagConstants.UPDATE;
    }

    /** 按消息业务主键回查 MySQL，再幂等更新 ES 投影。 */
    @Override
    protected void handle(MqMessage<MemberUserIndexEvent> message) {
        MemberUserIndexEvent event = message.getPayload();
        if (event == null || event.getMemberUserId() == null) {
            throw new IllegalArgumentException("会员用户索引事件缺少业务主键");
        }
        memberUserSearchService.upsertByMemberUserId(event.getMemberUserId());
        log.info("会员用户 ES 投影已更新，memberUserId={}, messageId={}", event.getMemberUserId(), message.getMessageId());
    }
}
