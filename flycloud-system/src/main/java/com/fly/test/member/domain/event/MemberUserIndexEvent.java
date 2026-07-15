package com.fly.test.member.domain.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员索引变更事件。
 *
 * <p>只传递业务主键和动作，不传递 MySQL Entity；消费者以主键回查权威数据。</p>
 */
@Data
public class MemberUserIndexEvent implements Serializable {

    private Long memberUserId;
    private String action;
}
