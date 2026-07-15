package com.fly.common.rocketmq.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.rocketmq.domain.MqConsumeRecord;
import org.apache.ibatis.annotations.Mapper;

/** MQ 消费幂等记录 Mapper。 */
@Mapper
public interface MqConsumeRecordMapper extends BaseMapperPlus<MqConsumeRecordMapper, MqConsumeRecord, MqConsumeRecord> {
}
