package com.fly.common.rocketmq.idempotent.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.rocketmq.idempotent.domain.MqConsumeRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * MQ 消费幂等记录 Mapper。
 */
@Mapper
public interface MqConsumeRecordMapper extends BaseMapperPlus<MqConsumeRecordMapper, MqConsumeRecord, MqConsumeRecord> {
}
