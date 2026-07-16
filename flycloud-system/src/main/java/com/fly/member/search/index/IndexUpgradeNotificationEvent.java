package com.fly.member.search.index;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 索引升级完成后发送给运维角色用户的通知消息。
 */
@Data
public class IndexUpgradeNotificationEvent implements Serializable {

    /**
     * 需要接收通知的后台用户编号集合。
     */
    private List<Long> userIds;

    /**
     * 索引升级完成后的通知正文。
     */
    private String content;
}
