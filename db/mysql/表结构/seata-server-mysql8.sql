-- ============================================================================
-- FlyCloud Seata Server 2.5.0 / MySQL 8.4 初始化脚本
--
-- 用途：创建 Seata TC（事务协调器）的元数据表。仅在 Seata Server 专用数据库执行一次。
-- 禁止在业务库执行本文件；业务库仅执行 seata-client-undo-log-mysql8.sql。
--
-- 执行账号：建议使用具备建库、建表权限的运维账号；运行时 Seata 使用最小权限专用账号。
-- 字符集：MySQL 8.4 使用 utf8mb4，避免旧 utf8（三字节）字符集限制。
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `fly_seata`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE `fly_seata`;

-- 全局事务会话表：每个 @GlobalTransactional 对应一条全局事务记录。
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128)  NOT NULL COMMENT '全局事务 ID',
    `transaction_id`            BIGINT        DEFAULT NULL COMMENT '事务 ID',
    `status`                    TINYINT       NOT NULL COMMENT '全局事务状态',
    `application_id`            VARCHAR(32)   DEFAULT NULL COMMENT '事务发起应用 ID',
    `transaction_service_group` VARCHAR(32)   DEFAULT NULL COMMENT '事务服务组',
    `transaction_name`          VARCHAR(128)  DEFAULT NULL COMMENT '事务名称',
    `timeout`                   INT           DEFAULT NULL COMMENT '超时时间（毫秒）',
    `begin_time`                BIGINT        DEFAULT NULL COMMENT '开始时间戳（毫秒）',
    `application_data`          VARCHAR(2000) DEFAULT NULL COMMENT '应用扩展数据',
    `gmt_create`                DATETIME      DEFAULT NULL COMMENT '创建时间',
    `gmt_modified`              DATETIME      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`xid`),
    KEY `idx_status_gmt_modified` (`status`, `gmt_modified`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata 全局事务表';

-- 分支事务会话表：记录每个业务服务参与的分支事务。
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT        NOT NULL COMMENT '分支事务 ID',
    `xid`               VARCHAR(128)  NOT NULL COMMENT '全局事务 ID',
    `transaction_id`    BIGINT        DEFAULT NULL COMMENT '事务 ID',
    `resource_group_id` VARCHAR(32)   DEFAULT NULL COMMENT '资源组 ID',
    `resource_id`       VARCHAR(256)  DEFAULT NULL COMMENT '资源 ID',
    `branch_type`       VARCHAR(8)    DEFAULT NULL COMMENT '分支事务类型，例如 AT',
    `status`            TINYINT       DEFAULT NULL COMMENT '分支事务状态',
    `client_id`         VARCHAR(64)   DEFAULT NULL COMMENT 'RM 客户端 ID',
    `application_data`  VARCHAR(2000) DEFAULT NULL COMMENT '应用扩展数据',
    `gmt_create`        DATETIME(6)   DEFAULT NULL COMMENT '创建时间',
    `gmt_modified`      DATETIME(6)   DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata 分支事务表';

-- 全局锁表：AT 模式对业务行加全局锁，防止并发事务写冲突。
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL COMMENT '全局锁行键',
    `xid`            VARCHAR(128)          DEFAULT NULL COMMENT '全局事务 ID',
    `transaction_id` BIGINT                DEFAULT NULL COMMENT '事务 ID',
    `branch_id`      BIGINT       NOT NULL COMMENT '分支事务 ID',
    `resource_id`    VARCHAR(256)          DEFAULT NULL COMMENT '资源 ID',
    `table_name`     VARCHAR(32)           DEFAULT NULL COMMENT '业务表名',
    `pk`             VARCHAR(36)           DEFAULT NULL COMMENT '业务表主键值',
    `status`         TINYINT      NOT NULL DEFAULT 0 COMMENT '0：锁定；1：回滚中',
    `gmt_create`     DATETIME              DEFAULT NULL COMMENT '创建时间',
    `gmt_modified`   DATETIME              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`row_key`),
    KEY `idx_branch_id` (`branch_id`),
    KEY `idx_status` (`status`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata 全局锁表';

-- 分布式锁表：多 TC 节点部署时用于协调定时任务与共享资源。
CREATE TABLE IF NOT EXISTS `distributed_lock`
(
    `lock_key`   CHAR(20)    NOT NULL COMMENT '锁键',
    `lock_value` VARCHAR(20) NOT NULL COMMENT '锁值',
    `expire`     BIGINT      DEFAULT NULL COMMENT '过期时间戳（毫秒）',
    PRIMARY KEY (`lock_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata Server 分布式锁表';

-- 官方预置锁键。INSERT IGNORE 使初始化脚本可安全重复执行。
INSERT IGNORE INTO `distributed_lock` (`lock_key`, `lock_value`, `expire`)
VALUES ('AsyncCommitting', ' ', 0),
       ('RetryCommitting', ' ', 0),
       ('RetryRollbacking', ' ', 0),
       ('TxTimeoutCheck', ' ', 0);

-- 事务服务组路由表：Seata Server 2.5.0 数据库存储模式所需的 vgroup 元数据表。
CREATE TABLE IF NOT EXISTS `vgroup_table`
(
    `vGroup`     VARCHAR(255) DEFAULT NULL COMMENT '事务服务组',
    `namespace`  VARCHAR(255) DEFAULT NULL COMMENT 'Nacos 命名空间',
    `cluster`    VARCHAR(255) DEFAULT NULL COMMENT 'Seata 集群名称',
    UNIQUE KEY `idx_vgroup_namespace_cluster` (`vGroup`, `namespace`, `cluster`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata 事务服务组路由表';
