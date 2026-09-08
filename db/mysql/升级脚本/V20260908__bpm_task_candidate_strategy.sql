-- BPM 审批人策略文案及发起人部门负责人策略字典。
-- 21 保持原有策略编号，仅调整为更明确的“指定部门负责人”；37 为发起人部门负责人。

UPDATE `fly-cloud`.sys_dict_data
SET label      = '指定部门负责人',
    remark     = '任务分配规则的类型 - 指定部门负责人',
    update_by  = '1',
    update_time = CURRENT_TIMESTAMP
WHERE dict_type = 'bpm_task_candidate_strategy'
  AND value = '21'
  AND is_deleted = 0;

UPDATE `fly-cloud`.sys_dict_data
SET sort        = 1,
    label       = '发起人部门负责人',
    remark      = '任务分配规则的类型 - 发起人部门负责人',
    update_by   = '1',
    update_time = CURRENT_TIMESTAMP
WHERE dict_type = 'bpm_task_candidate_strategy'
  AND value = '37'
  AND is_deleted = 0;

INSERT INTO `fly-cloud`.sys_dict_data
    (sort, label, value, dict_type, status, color_type, css_class, remark,
     create_by, create_time, update_by, update_time, is_deleted)
SELECT 1, '发起人部门负责人', '37', 'bpm_task_candidate_strategy', 0, 'primary', '',
       '任务分配规则的类型 - 发起人部门负责人', '1', CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 0
WHERE NOT EXISTS (
    SELECT 1
    FROM `fly-cloud`.sys_dict_data
    WHERE dict_type = 'bpm_task_candidate_strategy'
      AND value = '37'
      AND is_deleted = 0
);
