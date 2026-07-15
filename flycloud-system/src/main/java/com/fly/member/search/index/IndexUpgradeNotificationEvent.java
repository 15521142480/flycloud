package com.fly.member.search.index;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/** 索引升级完成后发送给运维角色用户的通知消息。 */
@Data
public class IndexUpgradeNotificationEvent implements Serializable {

    private List<Long> userIds;
    private String content;
}
