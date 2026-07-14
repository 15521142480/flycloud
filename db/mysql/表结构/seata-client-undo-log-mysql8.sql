-- ============================================================================
-- FlyCloud Seata Client 2.5.0 / MySQL 8.4 AT 模式 undo_log 初始化脚本
--
-- 用途：Seata RM 保存业务 SQL 的前后镜像，以便全局事务二阶段回滚。
-- 执行范围：每一个参与 Seata AT 分布式事务的业务数据库各执行一次。
-- 运行方式：随数据库迁移发布；禁止由应用启动时自动执行 DDL。
-- ============================================================================

CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT '分支事务 ID',
    `xid`           VARCHAR(128) NOT NULL COMMENT '全局事务 ID',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo 日志上下文，例如序列化方式',
    `rollback_info` LONGBLOB     NOT NULL COMMENT '回滚镜像数据',
    `log_status`    INT          NOT NULL COMMENT '0：正常；1：防御状态',
    `log_created`   DATETIME(6)  NOT NULL COMMENT '创建时间',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT '修改时间',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata AT 模式回滚日志表';
