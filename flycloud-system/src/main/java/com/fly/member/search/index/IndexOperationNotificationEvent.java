package com.fly.member.search.index;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员用户 Elasticsearch 索引操作完成通知消息。
 *
 * <p>用于初始化、全量同步和版本升级成功后的在线运维通知，不携带 Elasticsearch 或 MySQL 实体。</p>
 */
@Data
public class IndexOperationNotificationEvent implements Serializable {

    /** 需要接收通知的后台用户编号集合。 */
    private List<Long> userIds;

    /** 索引操作成功后的通知正文。 */
    private String content;
}
