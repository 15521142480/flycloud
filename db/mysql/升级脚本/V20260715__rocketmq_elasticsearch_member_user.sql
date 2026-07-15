-- RocketMQ 本地消息表、消费幂等表与会员用户 ES 索引升级字段。
-- 适用：MySQL 8.4.x；在所有使用 RocketMQ 的业务库执行本脚本。

CREATE TABLE IF NOT EXISTS `mq_outbox_message` (
    `id` BIGINT NOT NULL COMMENT '主键ID，雪花ID',
    `message_id` VARCHAR(128) NOT NULL COMMENT '业务消息唯一ID',
    `topic` VARCHAR(128) NOT NULL COMMENT 'MQ Topic',
    `tag` VARCHAR(128) NOT NULL COMMENT 'MQ Tag',
    `biz_key` VARCHAR(128) NOT NULL COMMENT '业务唯一标识',
    `payload` JSON NOT NULL COMMENT '标准消息信封JSON，不保存业务Entity',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '发送状态：0待发送，1已发送，2最终失败，3投递中（带租约）',
    `retry_count` INT NOT NULL DEFAULT 0 COMMENT '已重试次数',
    `next_retry_time` DATETIME(3) NOT NULL COMMENT '下一次投递时间',
    `dispatch_token` VARCHAR(64) DEFAULT NULL COMMENT '调度实例抢占令牌，用于防止并发或过期实例覆盖投递状态',
    `last_error` VARCHAR(1000) DEFAULT NULL COMMENT '最后一次投递异常',
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_message_id` (`message_id`),
    KEY `idx_status_retry_time` (`status`, `next_retry_time`),
    KEY `idx_biz_key` (`biz_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='MQ本地消息表';

-- 已执行过旧版脚本的库也必须补齐租约字段；MySQL 8.4 支持 IF NOT EXISTS。
ALTER TABLE `mq_outbox_message`
    ADD COLUMN IF NOT EXISTS `dispatch_token` VARCHAR(64) DEFAULT NULL COMMENT '调度实例抢占令牌，用于防止并发或过期实例覆盖投递状态' AFTER `next_retry_time`;

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

CREATE TABLE IF NOT EXISTS `member_user_index_upgrade_record` (
    `id` BIGINT NOT NULL COMMENT '主键ID，雪花ID',
    `alias` VARCHAR(128) NOT NULL COMMENT '业务索引别名',
    `old_index` VARCHAR(128) NOT NULL COMMENT '升级前真实索引',
    `new_index` VARCHAR(128) NOT NULL COMMENT '升级后真实索引',
    `status` VARCHAR(32) NOT NULL COMMENT 'CREATED、SYNCING、VALIDATING、SWITCHED、FAILED、ROLLED_BACK',
    `total_count` BIGINT DEFAULT NULL COMMENT 'MySQL 权威数据总数',
    `success_count` BIGINT DEFAULT NULL COMMENT '全量与补偿同步累计成功次数',
    `failed_count` BIGINT DEFAULT NULL COMMENT '同步失败数',
    `start_time` DATETIME(3) NOT NULL COMMENT '开始时间',
    `finish_time` DATETIME(3) DEFAULT NULL COMMENT '结束时间',
    `operator` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '操作人',
    `error_message` VARCHAR(2000) DEFAULT NULL COMMENT '失败原因',
    `rollback_time` DATETIME(3) DEFAULT NULL COMMENT '回滚时间',
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_alias_status` (`alias`, `status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员用户ES索引升级记录';
