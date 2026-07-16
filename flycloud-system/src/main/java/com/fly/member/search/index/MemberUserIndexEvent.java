package com.fly.member.search.index;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员索引变更事件。
 *
 * <p>只传递业务主键和动作，不传递 MySQL Entity；消费者以主键回查权威数据。</p>
 */
@Data
public class MemberUserIndexEvent implements Serializable {

    /**
     * 发生变化的会员用户主键。
     */
    private Long memberUserId;

    /**
     * 业务动作，例如 create、update、delete。
     */
    private String action;
}
