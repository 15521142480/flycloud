-- 会员用户 Elasticsearch 索引操作审计表重构。
-- 前置条件：已执行 V20260715__rocketmq_elasticsearch_member_user.sql。
-- 适用：MySQL 8.4.x；Flyway 版本脚本只执行一次，禁止重复手工执行。

-- 将升级专用审计表重命名为统一操作审计表，历史升级记录会被完整保留。
RENAME TABLE `member_user_index_upgrade_record` TO `member_user_index_operation_record`;

-- 旧记录默认均为 UPGRADE；原旧索引、新索引和状态字段改为通用操作语义。
ALTER TABLE `member_user_index_operation_record`
    ADD COLUMN `operation_type` VARCHAR(32) NOT NULL DEFAULT 'UPGRADE' COMMENT '操作类型：INITIALIZE、SYNCHRONIZE、UPGRADE、ROLLBACK、DELETE' AFTER `id`,
    CHANGE COLUMN `status` `operation_status` VARCHAR(32) NOT NULL COMMENT '执行状态：PENDING、RUNNING、SUCCEEDED、FAILED' AFTER `operation_type`,
    CHANGE COLUMN `old_index` `source_index` VARCHAR(128) DEFAULT NULL COMMENT '操作前真实版本索引；首次初始化为空' AFTER `alias`,
    CHANGE COLUMN `new_index` `target_index` VARCHAR(128) NOT NULL COMMENT '操作目标真实版本索引' AFTER `source_index`,
    ADD COLUMN `target_index_status` VARCHAR(32) NOT NULL DEFAULT 'AVAILABLE' COMMENT '目标版本状态：AVAILABLE、DELETED' AFTER `target_index`,
    ADD COLUMN `rollback_status` VARCHAR(32) NOT NULL DEFAULT 'NOT_APPLICABLE' COMMENT '升级回滚状态：NOT_APPLICABLE、AVAILABLE、ROLLED_BACK、UNAVAILABLE' AFTER `target_index_status`,
    ADD COLUMN `rollback_unavailable_reason` VARCHAR(500) DEFAULT NULL COMMENT '不可回滚原因，例如升级前版本索引已删除' AFTER `rollback_status`;

-- 将旧升级状态转换为通用执行状态，同时保留已切换记录的回滚可用性。
UPDATE `member_user_index_operation_record`
SET `rollback_status` = CASE `operation_status`
    WHEN 'SWITCHED' THEN 'AVAILABLE'
    WHEN 'ROLLED_BACK' THEN 'ROLLED_BACK'
    ELSE 'NOT_APPLICABLE'
END;

UPDATE `member_user_index_operation_record`
SET `operation_status` = CASE `operation_status`
    WHEN 'CREATED' THEN 'PENDING'
    WHEN 'SYNCING' THEN 'RUNNING'
    WHEN 'VALIDATING' THEN 'RUNNING'
    WHEN 'SWITCHED' THEN 'SUCCEEDED'
    WHEN 'ROLLED_BACK' THEN 'SUCCEEDED'
    ELSE `operation_status`
END;

ALTER TABLE `member_user_index_operation_record`
    DROP INDEX `idx_alias_status`,
    ADD KEY `idx_alias_operation_status` (`alias`, `operation_status`),
    ADD KEY `idx_target_index` (`target_index`),
    ADD KEY `idx_source_index` (`source_index`);

ALTER TABLE `member_user_index_operation_record`
    COMMENT = '会员用户Elasticsearch索引操作审计记录';
