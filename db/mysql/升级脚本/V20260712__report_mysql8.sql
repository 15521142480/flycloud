-- FlyCloud 报表模块初始化脚本（MySQL 8.4.10）
-- 包含：JimuReport 2.3.4、JimuBI 2.3.2、GoView、管理菜单和角色授权。
-- 可重复执行；不会删除已有表或导入官方演示数据。

CREATE TABLE IF NOT EXISTS `jimu_dict` (
  `id` varchar(32) NOT NULL,
  `dict_name` varchar(100) DEFAULT NULL COMMENT '字典名称',
  `dict_code` varchar(100) DEFAULT NULL COMMENT '字典编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `del_flag` int DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `type` int DEFAULT '0' COMMENT '字典类型0为string,1为number',
  `tenant_id` varchar(10) DEFAULT NULL COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_sd_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `jimu_dict_item` (
  `id` varchar(32) NOT NULL,
  `dict_id` varchar(32) DEFAULT NULL COMMENT '字典id',
  `item_text` varchar(100) DEFAULT NULL COMMENT '字典项文本',
  `item_value` varchar(100) DEFAULT NULL COMMENT '字典项值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort_order` int DEFAULT NULL COMMENT '排序',
  `status` int DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_sdi_role_dict_id` (`dict_id`) USING BTREE,
  KEY `idx_sdi_role_sort_order` (`sort_order`) USING BTREE,
  KEY `idx_sdi_status` (`status`) USING BTREE,
  KEY `idx_sdi_dict_val` (`dict_id`,`item_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `jimu_report` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(50) DEFAULT NULL COMMENT '编码',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `note` varchar(255) DEFAULT NULL COMMENT '说明',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `json_str` longtext COMMENT 'json字符串',
  `api_url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `thumb` text COMMENT '缩略图',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint DEFAULT NULL COMMENT '删除标识0-正常,1-已删除',
  `api_method` varchar(255) DEFAULT NULL COMMENT '请求方法0-get,1-post',
  `api_code` varchar(255) DEFAULT NULL COMMENT '请求编码',
  `template` tinyint DEFAULT NULL COMMENT '是否是模板 0-是,1-不是',
  `view_count` bigint DEFAULT '0' COMMENT '浏览次数',
  `css_str` text COMMENT 'css增强',
  `js_str` text COMMENT 'js增强',
  `py_str` text COMMENT 'py增强',
  `tenant_id` varchar(10) DEFAULT NULL COMMENT '多租户标识',
  `update_count` int DEFAULT '0' COMMENT '乐观锁版本',
  `submit_form` tinyint DEFAULT NULL COMMENT '是否填报报表 0不是,1是',
  `is_multi_sheet` tinyint DEFAULT NULL COMMENT '是否多sheet报表 1是 0否',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_jmreport_code` (`code`) USING BTREE,
  KEY `uniq_jmreport_createby` (`create_by`) USING BTREE,
  KEY `uniq_jmreport_delflag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='在线excel设计器';

CREATE TABLE IF NOT EXISTS `jimu_report_category` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级id',
  `iz_leaf` int DEFAULT NULL COMMENT '是否为叶子节点(0 否 1是)',
  `source_type` varchar(10) DEFAULT NULL COMMENT '来源类型( report 积木报表 screen 大屏  drag 仪表盘)',
  `create_by` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(11) DEFAULT NULL COMMENT '租户id',
  `del_flag` int DEFAULT NULL COMMENT '删除状态(0未删除，1已删除，2临时删除)',
  `sort_no` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分类';

CREATE TABLE IF NOT EXISTS `jimu_report_data_source` (
  `id` varchar(36) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '数据源名称',
  `report_id` varchar(100) DEFAULT NULL COMMENT '报表_id',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `db_type` varchar(10) DEFAULT NULL COMMENT '数据库类型',
  `db_driver` varchar(100) DEFAULT NULL COMMENT '驱动类',
  `db_url` varchar(500) DEFAULT NULL COMMENT '数据源地址',
  `db_username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `db_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `connect_times` int DEFAULT '0' COMMENT '连接失败次数',
  `tenant_id` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '多租户标识',
  `type` varchar(10) DEFAULT NULL COMMENT '类型(report:报表;drag:仪表盘)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_jmdatasource_report_id` (`report_id`) USING BTREE,
  KEY `idx_jmdatasource_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `jimu_report_db` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `jimu_report_id` varchar(32) DEFAULT NULL COMMENT '主键字段',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名称',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `db_code` varchar(32) DEFAULT NULL COMMENT '数据集编码',
  `db_ch_name` varchar(50) DEFAULT NULL COMMENT '数据集名字',
  `db_type` varchar(32) DEFAULT NULL COMMENT '数据源类型',
  `db_table_name` varchar(32) DEFAULT NULL COMMENT '数据库表名',
  `db_dyn_sql` longtext COMMENT '动态查询SQL',
  `db_key` varchar(32) DEFAULT NULL COMMENT '数据源KEY',
  `tb_db_key` varchar(32) DEFAULT NULL COMMENT '填报数据源',
  `tb_db_table_name` varchar(32) DEFAULT NULL COMMENT '填报数据表',
  `java_type` varchar(32) DEFAULT NULL COMMENT 'java类数据集  类型（spring:springkey,class:java类名）',
  `java_value` varchar(255) DEFAULT NULL COMMENT 'java类数据源  数值（bean key/java类名）',
  `api_url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `api_method` varchar(255) DEFAULT NULL COMMENT '请求方法0-get,1-post',
  `is_list` varchar(10) DEFAULT '0' COMMENT '是否是列表0否1是 默认0',
  `is_page` varchar(10) DEFAULT NULL COMMENT '是否作为分页,0:不分页，1:分页',
  `db_source` varchar(255) DEFAULT NULL COMMENT '数据源',
  `db_source_type` varchar(50) DEFAULT NULL COMMENT '数据库类型 MYSQL ORACLE SQLSERVER',
  `json_data` text COMMENT 'json数据，直接解析json内容',
  `api_convert` varchar(255) DEFAULT NULL COMMENT 'api转换器',
  `iz_shared_source` int DEFAULT NULL COMMENT '是否为共享数据源(0 否 1 是)',
  `jimu_shared_source_id` varchar(32) DEFAULT NULL COMMENT '指向共享数据集的id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_jmreportdb_db_key` (`db_key`) USING BTREE,
  KEY `idx_jimu_report_id` (`jimu_report_id`) USING BTREE,
  KEY `idx_db_source_id` (`db_source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `jimu_report_db_field` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `jimu_report_db_id` varchar(32) DEFAULT NULL COMMENT '数据源ID',
  `field_name` varchar(80) DEFAULT NULL COMMENT '字段名',
  `field_name_physics` varchar(200) DEFAULT NULL COMMENT '物理字段名（文件数据集使用，存的是excel的字段标题）',
  `field_text` varchar(50) DEFAULT NULL COMMENT '字段文本',
  `widget_type` varchar(50) DEFAULT NULL COMMENT '控件类型',
  `widget_width` int DEFAULT NULL COMMENT '控件宽度',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `search_flag` int DEFAULT '0' COMMENT '查询标识0否1是 默认0',
  `search_mode` int DEFAULT NULL COMMENT '查询模式1简单2范围',
  `dict_code` varchar(255) DEFAULT NULL COMMENT '字典编码支持从表中取数据',
  `search_value` varchar(100) DEFAULT NULL COMMENT '查询默认值',
  `search_format` varchar(50) DEFAULT NULL COMMENT '查询时间格式化表达式',
  `ext_json` text COMMENT '参数配置',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_jrdf_jimu_report_db_id` (`jimu_report_db_id`) USING BTREE,
  KEY `idx_dbfield_order_num` (`order_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `jimu_report_db_param` (
  `id` varchar(36) NOT NULL,
  `jimu_report_head_id` varchar(36) NOT NULL COMMENT '动态报表ID',
  `param_name` varchar(32) NOT NULL COMMENT '参数字段',
  `param_txt` varchar(32) DEFAULT NULL COMMENT '参数文本',
  `param_value` varchar(1000) DEFAULT NULL COMMENT '参数默认值',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `search_flag` int DEFAULT NULL COMMENT '查询标识0否1是 默认0',
  `widget_type` varchar(50) DEFAULT NULL COMMENT '查询控件类型',
  `search_mode` int DEFAULT NULL COMMENT '查询模式1简单2范围',
  `dict_code` varchar(255) DEFAULT NULL COMMENT '字典',
  `search_format` varchar(50) DEFAULT NULL COMMENT '查询时间格式化表达式',
  `ext_json` text COMMENT '参数配置',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_jrdp_jimu_report_head_id` (`jimu_report_head_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `jimu_report_export_job` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务名称',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `exec_interval` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '执行频率',
  `report_conf` text COLLATE utf8mb4_unicode_ci COMMENT '导出报表配置',
  `last_run_time` datetime DEFAULT NULL COMMENT '最后执行时间',
  `receiver_email` text COLLATE utf8mb4_unicode_ci COMMENT '接收通知的邮件',
  `file_sync_path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件同步路径',
  `status` int DEFAULT NULL COMMENT '状态(0:停止;1:启动)',
  `create_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `tenant_id` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '多租户标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积木报表导出计划表';

CREATE TABLE IF NOT EXISTS `jimu_report_export_log` (
  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次编号',
  `export_channel` varchar(20) DEFAULT NULL COMMENT '导出渠道',
  `export_from` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发起来源',
  `from_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '来源id',
  `export_type` varchar(10) DEFAULT NULL COMMENT '导出类型',
  `report_id` text COMMENT '报表id',
  `download_path` varchar(255) DEFAULT NULL COMMENT '下载路径',
  `status` varchar(15) DEFAULT NULL COMMENT '状态',
  `err_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '错误消息',
  `create_by` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '多租户标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积木报表自动导出记录表';

CREATE TABLE IF NOT EXISTS `jimu_report_ext_data` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `biz_type` varchar(100) NOT NULL COMMENT '业务类型标识，如 report_share、temp_config 等',
  `name` varchar(200) DEFAULT NULL COMMENT '名称，展示用',
  `descr` varchar(500) DEFAULT NULL COMMENT '描述信息',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签，多个用逗号分隔',
  `data_value` longtext COMMENT '实际存储内容',
  `metadata` varchar(500) DEFAULT NULL COMMENT '元数据，用于存储补充信息',
  `status` tinyint DEFAULT '1' COMMENT '状态标识：1正常 0禁用',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_biz` (`biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用扩展数据表';

CREATE TABLE IF NOT EXISTS `jimu_report_icon_lib` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '图片名称',
  `type` varchar(32) DEFAULT NULL COMMENT '图片类型',
  `image_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '图片地址',
  `create_by` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='积木图库表';

CREATE TABLE IF NOT EXISTS `jimu_report_link` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `report_id` varchar(32) DEFAULT NULL COMMENT '积木设计器id',
  `parameter` text COMMENT '参数',
  `eject_type` varchar(1) DEFAULT NULL COMMENT '弹出方式（0 当前页面 1 新窗口）',
  `link_name` varchar(255) DEFAULT NULL COMMENT '链接名称',
  `api_method` varchar(1) DEFAULT NULL COMMENT '请求方法0-get,1-post',
  `link_type` varchar(1) DEFAULT NULL COMMENT '链接方式(0 网络报表 1 网络连接 2 图表联动)',
  `api_url` varchar(1000) DEFAULT NULL COMMENT '外网api',
  `link_chart_id` varchar(50) DEFAULT NULL COMMENT '联动图表的ID',
  `expression` varchar(255) DEFAULT NULL COMMENT '表达式',
  `requirement` varchar(255) DEFAULT NULL COMMENT '条件',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uniq_link_reportid` (`report_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='超链接配置表';

CREATE TABLE IF NOT EXISTS `jimu_report_map` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `label` varchar(125) DEFAULT NULL COMMENT '地图名称',
  `name` varchar(125) DEFAULT NULL COMMENT '地图编码',
  `data` longtext COMMENT '地图数据',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '0表示未删除,1表示删除',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_jmreport_map_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='地图配置表';

CREATE TABLE IF NOT EXISTS `jimu_report_share` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `report_id` varchar(32) DEFAULT NULL COMMENT '在线excel设计器id',
  `preview_url` varchar(1000) DEFAULT NULL COMMENT '预览地址',
  `preview_lock` varchar(4) DEFAULT NULL COMMENT '密码锁',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `term_of_validity` varchar(1) DEFAULT NULL COMMENT '有效期(0:永久有效，1:1天，2:7天)',
  `status` varchar(1) DEFAULT NULL COMMENT '是否过期(0未过期，1已过期)',
  `preview_lock_status` varchar(1) DEFAULT NULL COMMENT '密码锁状态(0不存在密码锁，1存在密码锁)',
  `share_token` varchar(50) DEFAULT NULL COMMENT '分享token',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_report_id` (`report_id`),
  KEY `idx_jrs_share_token` (`share_token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='积木报表预览权限表';

CREATE TABLE IF NOT EXISTS `jimu_report_sheet` (
  `id` varchar(64) NOT NULL COMMENT '主键（Sheet ID）',
  `report_id` varchar(64) NOT NULL COMMENT '报表ID',
  `sheet_name` varchar(255) NOT NULL COMMENT 'Sheet名称',
  `sheet_order` int NOT NULL COMMENT '排序（可以为负数，负数表示在默认sheet前面）',
  `json_str` longtext COMMENT '该sheet的完整jsonStr',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_sheet_order` (`report_id`,`sheet_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表Sheet表';

CREATE TABLE IF NOT EXISTS `onl_drag_comp` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `parent_id` varchar(32) DEFAULT NULL,
  `comp_name` varchar(50) DEFAULT NULL COMMENT '组件名称',
  `comp_type` varchar(20) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `type_id` int DEFAULT NULL COMMENT '组件类型',
  `comp_config` longtext COMMENT '组件配置',
  `status` varchar(2) CHARACTER SET utf8mb4 DEFAULT '0' COMMENT '状态0:无效 1:有效',
  `create_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组件库';

CREATE TABLE IF NOT EXISTS `onl_drag_dataset_head` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `code` varchar(36) DEFAULT NULL COMMENT '编码',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父id',
  `db_source` varchar(100) DEFAULT NULL COMMENT '动态数据源',
  `query_sql` varchar(5000) DEFAULT '0' COMMENT '查询数据SQL',
  `content` varchar(1000) DEFAULT NULL COMMENT '描述',
  `iz_agent` varchar(10) DEFAULT '0' COMMENT 'iz_agent',
  `data_type` varchar(50) DEFAULT NULL COMMENT '数据类型',
  `api_method` varchar(10) DEFAULT NULL COMMENT 'api方法：get/post',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `low_app_id` varchar(32) DEFAULT NULL COMMENT '应用ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `onl_drag_dataset_item` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `head_id` varchar(36) NOT NULL COMMENT '主表ID',
  `field_name` varchar(36) DEFAULT NULL COMMENT '字段名',
  `field_txt` varchar(1000) DEFAULT NULL COMMENT '字段文本',
  `field_type` varchar(10) DEFAULT NULL COMMENT '字段类型',
  `widget_type` varchar(30) DEFAULT NULL COMMENT '控件类型',
  `dict_code` varchar(500) DEFAULT NULL COMMENT '字典Code',
  `dict_table` varchar(125) DEFAULT NULL,
  `dict_text` varchar(125) DEFAULT NULL,
  `iz_show` varchar(5) DEFAULT NULL COMMENT '是否列表显示',
  `iz_search` varchar(10) DEFAULT NULL COMMENT '是否查询',
  `iz_total` varchar(5) DEFAULT NULL COMMENT '是否计算总计（仅对数值有效）',
  `search_mode` varchar(10) DEFAULT NULL COMMENT '查询模式',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_oddi_head_id` (`head_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `onl_drag_dataset_param` (
  `id` varchar(36) NOT NULL,
  `head_id` varchar(36) NOT NULL COMMENT '动态报表ID',
  `param_name` varchar(32) NOT NULL COMMENT '参数字段',
  `param_txt` varchar(32) DEFAULT NULL COMMENT '参数文本',
  `param_value` varchar(1000) DEFAULT NULL COMMENT '参数默认值',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `iz_search` int DEFAULT NULL COMMENT '查询标识0否1是 默认0',
  `widget_type` varchar(50) DEFAULT NULL COMMENT '查询控件类型',
  `search_mode` int DEFAULT NULL COMMENT '查询模式1简单2范围',
  `dict_code` varchar(255) DEFAULT NULL COMMENT '字典',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_oddp_head_id` (`head_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS `onl_drag_page` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '界面名称',
  `path` varchar(100) DEFAULT NULL COMMENT '访问路径',
  `background_color` varchar(10) DEFAULT NULL COMMENT '背景色',
  `background_image` varchar(255) DEFAULT NULL COMMENT '背景图',
  `design_type` int DEFAULT NULL COMMENT '设计模式(1:pc,2:手机,3:平板)',
  `theme` varchar(10) DEFAULT NULL COMMENT '主题色',
  `style` varchar(20) DEFAULT NULL COMMENT '面板主题',
  `cover_url` varchar(500) DEFAULT NULL COMMENT '封面图',
  `des_json` varchar(1000) DEFAULT NULL COMMENT '仪表盘主配置JSON',
  `template` longtext COMMENT '布局json',
  `protection_code` varchar(32) DEFAULT NULL COMMENT '保护码',
  `type` varchar(64) DEFAULT NULL COMMENT '文件夹类',
  `iz_template` varchar(10) DEFAULT '0' COMMENT '是否模板(1:是；0不是)',
  `create_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `low_app_id` varchar(50) DEFAULT NULL COMMENT '应用ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `update_count` int DEFAULT '1',
  `visits_num` int DEFAULT NULL COMMENT '访问次数',
  `del_flag` int DEFAULT NULL COMMENT '删除状态( 0未删除 1已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='可视化拖拽界面';

CREATE TABLE IF NOT EXISTS `onl_drag_page_comp` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父组件ID',
  `page_Id` varchar(50) DEFAULT NULL COMMENT '界面ID',
  `comp_id` varchar(32) DEFAULT NULL COMMENT '组件库ID',
  `component` varchar(50) DEFAULT NULL COMMENT '组件名称',
  `config` longtext COMMENT '组件配置',
  `create_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='可视化拖拽页面组件';

CREATE TABLE IF NOT EXISTS `onl_drag_share` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `drag_id` varchar(32) DEFAULT NULL COMMENT '在线仪表盘设计器id',
  `preview_url` varchar(1000) DEFAULT NULL COMMENT '预览地址',
  `preview_lock` varchar(4) DEFAULT NULL COMMENT '密码锁',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `term_of_validity` varchar(1) DEFAULT NULL COMMENT '有效期(0:永久有效，1:1天，7:7天)',
  `status` varchar(1) DEFAULT NULL COMMENT '是否过期(0未过期，1已过期)',
  `preview_lock_status` varchar(1) DEFAULT NULL COMMENT '是否为密码锁(0 否,1是)',
  `share_token` varchar(32) DEFAULT NULL COMMENT '分享token',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_ods_drag_id` (`drag_id`) USING BTREE COMMENT '仪表盘id唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='仪表盘预览分享表';

CREATE TABLE IF NOT EXISTS `onl_drag_table_relation` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `aggregation_name` varchar(100) DEFAULT NULL COMMENT '聚合表名称',
  `aggregation_desc` varchar(100) DEFAULT NULL COMMENT '聚合表描述',
  `relation_forms` longtext COMMENT '关联表单',
  `filter_condition` longtext COMMENT '过滤条件',
  `header_fields` longtext COMMENT '表头字段',
  `calculate_fields` longtext COMMENT '公式字段',
  `validate_info` longtext COMMENT '校验信息',
  `del_flag` tinyint DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `low_app_id` varchar(50) DEFAULT NULL COMMENT '应用ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_aggregation_name` (`aggregation_name`) USING BTREE,
  KEY `idx_del_flag` (`del_flag`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE,
  KEY `idx_create_by` (`create_by`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='仪表盘聚合表';

-- GoView 表、菜单与角色授权：可重复执行，用于已初始化的 fly-cloud 数据库。

CREATE TABLE IF NOT EXISTS report_go_view_project
(
    id          bigint auto_increment comment '项目编号'
        primary key,
    name        varchar(255)                       not null comment '项目名称',
    pic_url     varchar(1024)                      null comment '预览图片 URL',
    content     longtext                           null comment '报表内容（JSON）',
    status      tinyint  default 1                 not null comment '发布状态（0：已发布，1：未发布）',
    remark      varchar(500)                       null comment '项目备注',
    create_by   varchar(32)                        null comment '创建人',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(32)                        null comment '更新人',
    update_time datetime                           null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  bit      default b'0'              not null comment '是否删除'
)
    comment 'GoView 项目表' charset = utf8mb4;

UPDATE sys_menu
SET component_name = 'JimuReport'
WHERE id = 1282;

UPDATE sys_menu
SET name = 'GoView 大屏设计器', component_name = 'GoView', sort = 3
WHERE id = 2153;

INSERT INTO sys_menu (id, type, name, parent_id, permission, button_permission, sort, level, path, icon,
                      component, component_name, status, visible, keep_alive, always_show, target, remark,
                      create_by, create_time, is_deleted)
SELECT 2809, 0, '积木仪表盘', 1281, '', NULL, 2, 2, 'jimu-bi', 'ep:histogram',
       'report/jmreport/bi', 'JimuBI', 0, TRUE, TRUE, TRUE, FALSE, NULL,
       '1', NOW(), FALSE
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 2809);

-- 继承已拥有“报表管理”菜单的角色，确保三个报表入口都可见。
INSERT INTO sys_role_menu (role_id, menu_id, permission)
SELECT source.role_id, target.menu_id, NULL
FROM (
    SELECT DISTINCT role_id
    FROM sys_role_menu
    WHERE menu_id = 1281
) source
CROSS JOIN (
    SELECT 1282 AS menu_id
    UNION ALL SELECT 2809
    UNION ALL SELECT 2153
) target
WHERE NOT EXISTS (
    SELECT 1
    FROM sys_role_menu assigned
    WHERE assigned.role_id = source.role_id
      AND assigned.menu_id = target.menu_id
);
