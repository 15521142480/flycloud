package com.fly.common.rocketmq.outbox.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.rocketmq.outbox.domain.MqOutboxMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 本地消息表 Mapper。
 */
@Mapper
public interface MqOutboxMessageMapper extends BaseMapperPlus<MqOutboxMessageMapper, MqOutboxMessage, MqOutboxMessage> {

    /**
     * 查询可投递的待发送消息。
     *
     * @param pendingStatus 待投递状态编码
     * @param sendingStatus 投递中状态编码；租约到期后允许重新抢占
     * @param limit 本轮最大读取数量
     * @return 待投递消息，按主键顺序返回
     */
    @Select("""
            SELECT * FROM mq_outbox_message
            WHERE status IN (#{pendingStatus}, #{sendingStatus}) AND next_retry_time <= NOW(3)
            ORDER BY id ASC LIMIT #{limit}
            """)
    List<MqOutboxMessage> selectPendingMessages(@Param("pendingStatus") int pendingStatus,
                                                 @Param("sendingStatus") int sendingStatus,
                                                 @Param("limit") int limit);
}
