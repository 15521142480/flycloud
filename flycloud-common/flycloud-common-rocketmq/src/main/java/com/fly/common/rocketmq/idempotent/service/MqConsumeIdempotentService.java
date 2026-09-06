package com.fly.common.rocketmq.idempotent.service;

import com.fly.common.rocketmq.idempotent.domain.MqConsumeRecord;
import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import com.fly.common.rocketmq.idempotent.mapper.MqConsumeRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 消费幂等服务；消费记录与业务处理放在同一事务中。
 */
@RequiredArgsConstructor
public class MqConsumeIdempotentService {

    private final MqConsumeRecordMapper mapper;

    /**
     * 首次消费时执行业务处理。重复消息忽略；异常回滚消费记录并交给 RocketMQ 重试。
     *
     * note：consumerGroup 和 messageId 是唯一索引，即不同消费组（比如服不同务）可消费同一个信息；比如更新了部门信息：系统服务的系统部门更新、用户服务的用户部门更新
     *
     * <p>若 ES 写入成功但本地事务提交失败，RocketMQ 重放后会再次执行幂等 upsert/delete，因此不会造成数据错误。</p>
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean consume(String consumerGroup, String topic, String tag, MqMessage<?> message, Runnable businessHandler) {
        MqConsumeRecord record = new MqConsumeRecord();
        record.setConsumerGroup(consumerGroup);
        record.setMessageId(message.getMessageId());
        record.setTopic(topic);
        record.setTag(tag);
        record.setBizKey(message.getBizKey());
        record.setConsumeTime(LocalDateTime.now());
        record.setCreateBy("rocketmq");
        record.setCreateTime(LocalDateTime.now());
        record.setIsDeleted(false);
        try {
            mapper.insert(record);
        } catch (DuplicateKeyException ignored) {
            return false;
        }
        businessHandler.run();
        return true;
    }
}
