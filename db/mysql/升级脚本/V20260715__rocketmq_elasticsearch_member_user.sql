-- RocketMQ 本地消息表、消费幂等表与会员用户 ES 索引升级字段。
-- 适用：MySQL 8.4.x；在所有使用 RocketMQ 的业务库执行本脚本。

create table mq_outbox_message
(
    id              bigint                                   not null comment '主键ID，雪花ID'
        primary key,
    message_id      varchar(128)                             not null comment '业务消息唯一ID',
    topic           varchar(128)                             not null comment 'MQ Topic',
    tag             varchar(128)                             not null comment 'MQ Tag',
    biz_key         varchar(128)                             not null comment '业务唯一标识',
    payload         json                                     not null comment '标准消息信封JSON，不保存业务Entity',
    status          tinyint     default 0                    not null comment '发送状态：0待发送，1已发送，2最终失败，3投递中（带租约）',
    retry_count     int         default 0                    not null comment '已重试次数',
    next_retry_time datetime(3)                              not null comment '下一次投递时间',
    dispatch_token  varchar(64)                              null comment '调度实例抢占令牌，用于防止并发或过期实例覆盖投递状态',
    last_error      varchar(1000)                            null comment '最后一次投递异常',
    create_time     datetime(3) default CURRENT_TIMESTAMP(3) not null comment '创建时间',
    update_time     datetime(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3) comment '更新时间',
    constraint uk_message_id
        unique (message_id)
)
    comment 'MQ本地消息表';

create index idx_biz_key
    on mq_outbox_message (biz_key);

create index idx_status_retry_time
    on mq_outbox_message (status, next_retry_time);



CREATE TABLE IF NOT EXISTS `mq_consume_record` (
    `id` BIGINT NOT NULL COMMENT '主键ID，雪花ID',
    `consumer_group` VARCHAR(128) NOT NULL COMMENT '消费者组',
    `message_id` VARCHAR(128) NOT NULL COMMENT '业务消息唯一ID',
    `topic` VARCHAR(128) NOT NULL COMMENT 'MQ Topic；主题，例如用户域 user-event',
    `tag` VARCHAR(128) DEFAULT NULL COMMENT 'MQ Tag；事件标识，例如 update',
    `biz_key` VARCHAR(128) DEFAULT NULL COMMENT '业务唯一标识，例如订单号',
    `consume_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '消费成功时间',
    `create_by` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `is_deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_consumer_group_message_id` (`consumer_group`, `message_id`),
    KEY `idx_biz_key` (`biz_key`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MQ消息消费幂等记录表';


-- DDL 由升级脚本统一管理；索引迁移接口不在运行期执行 ALTER TABLE。
ALTER TABLE `member_user`
    ADD COLUMN IF NOT EXISTS `post_ids` VARCHAR(256) NULL COMMENT '岗位编号数组' AFTER `tag_ids`;
