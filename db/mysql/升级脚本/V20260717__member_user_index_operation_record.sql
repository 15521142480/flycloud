-- 会员用户 Elasticsearch 索引操作审计表重构。
-- 前置条件：已执行 V20260715__rocketmq_elasticsearch_member_user.sql。
-- 适用：MySQL 8.4.x；Flyway 版本脚本只执行一次，禁止重复手工执行。

create table member_user_index_operation_record
(
    id                          bigint                                   not null comment '主键ID，雪花ID'
        primary key,
    operation_type              varchar(32) default 'UPGRADE'            not null comment '操作类型：INITIALIZE、SYNCHRONIZE、UPGRADE、ROLLBACK、DELETE',
    operation_status            varchar(32)                              not null comment '执行状态：PENDING、RUNNING、SUCCEEDED、FAILED',
    alias                       varchar(128)                             not null comment '业务索引别名',
    source_index                varchar(128)                             null comment '操作前真实版本索引；首次初始化为空',
    target_index                varchar(128)                             not null comment '操作目标真实版本索引',
    target_index_status         varchar(32) default 'AVAILABLE'          not null comment '目标版本状态：AVAILABLE、DELETED',
    rollback_status             varchar(32) default 'NOT_APPLICABLE'     not null comment '升级回滚状态：NOT_APPLICABLE、AVAILABLE、ROLLED_BACK、UNAVAILABLE',
    rollback_unavailable_reason varchar(500)                             null comment '不可回滚原因，例如升级前版本索引已删除',
    total_count                 bigint                                   null comment 'MySQL 权威数据总数',
    success_count               bigint                                   null comment '全量与补偿同步累计成功次数',
    failed_count                bigint                                   null comment '同步失败数',
    start_time                  datetime(3)                              not null comment '开始时间',
    finish_time                 datetime(3)                              null comment '结束时间',
    operator                    varchar(64) default ''                   not null comment '操作人',
    error_message               varchar(2000)                            null comment '失败原因',
    rollback_time               datetime(3)                              null comment '回滚时间',
    create_time                 datetime(3) default CURRENT_TIMESTAMP(3) not null comment '创建时间',
    update_time                 datetime(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3) comment '更新时间'
)
    comment '会员用户Elasticsearch索引操作审计记录';

create index idx_alias_operation_status
    on member_user_index_operation_record (alias, operation_status);

create index idx_create_time
    on member_user_index_operation_record (create_time);

create index idx_source_index
    on member_user_index_operation_record (source_index);

create index idx_target_index
    on member_user_index_operation_record (target_index);


