package com.fly.common.rocketmq.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.rocketmq.domain.MqOutboxMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 本地消息表 Mapper。 */
@Mapper
public interface MqOutboxMessageMapper extends BaseMapperPlus<MqOutboxMessageMapper, MqOutboxMessage, MqOutboxMessage> {

    /**
     * 查询可投递的待发送消息。
     *
     * @param limit 本轮最大读取数量
     * @return 待投递消息，按主键顺序返回
     */
    @Select("""
            SELECT * FROM mq_outbox_message
            WHERE status IN (0, 3) AND next_retry_time <= NOW(3)
            ORDER BY id ASC LIMIT #{limit}
            """)
    List<MqOutboxMessage> selectPendingMessages(int limit);
}
