/*
 Navicat Premium Data Transfer

 Source Server         : mysql_one
 Source Server Type    : MySQL
 Source Server Version : 50736 (5.7.36)
 Source Host           : 39.98.125.88:3306
 Source Schema         : fly-cloud

 Target Server Type    : MySQL
 Target Server Version : 50736 (5.7.36)
 File Encoding         : 65001

 Date: 03/09/2024 18:54:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL COMMENT '编号',
  `table_id` bigint(20) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) NOT NULL COMMENT '接口编码',
  `name` varchar(100) DEFAULT NULL COMMENT '接口名称',
  `notes` varchar(200) DEFAULT NULL COMMENT '接口描述',
  `method` varchar(20) DEFAULT NULL COMMENT '请求方法',
  `class_name` varchar(255) DEFAULT NULL COMMENT '类名',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `path` varchar(255) DEFAULT NULL COMMENT '请求路径',
  `content_type` varchar(100) DEFAULT NULL COMMENT '响应类型',
  `service_id` varchar(100) DEFAULT NULL COMMENT '服务ID',
  `status` char(1) DEFAULT '0' COMMENT 'API状态:0:启用 1:禁用',
  `auth` char(1) DEFAULT '0' COMMENT '是否认证:0:不认证 1:认证',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` char(1) DEFAULT '0' COMMENT '删除标识',
  `tenant_id` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统接口表';

-- ----------------------------
-- Records of sys_api
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `storage_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '存储ID',
  `attachment_group_id` int(11) NOT NULL DEFAULT '0' COMMENT '组ID',
  `name` varchar(128) NOT NULL COMMENT '文件名称',
  `size` int(11) NOT NULL COMMENT '文件大小',
  `url` varchar(2080) NOT NULL COMMENT '文件地址',
  `file_name` varchar(200) DEFAULT NULL COMMENT '上传文件名',
  `thumb_url` varchar(2080) NOT NULL DEFAULT '' COMMENT '缩略图地址',
  `type` tinyint(2) NOT NULL COMMENT '类型',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` char(1) DEFAULT '0' COMMENT '删除标识',
  `is_recycle` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否加入回收站 0.否|1.是',
  PRIMARY KEY (`id`),
  KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='附件表';

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
BEGIN;
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (13, 0, 0, 'timg (3).jpeg', 26516, 'https://cdn.ckjia.com/10e258da699b4c318ff59e24f2599420.jpeg', '10e258da699b4c318ff59e24f2599420.jpeg', '', 1, NULL, NULL, '2020-08-10 03:29:26', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (16, 0, 0, 'background.jpg', 261548, 'https://cdn.ckjia.com/3b8f8e2b5ffb43b0905397b82a6b3ec6.jpg', '3b8f8e2b5ffb43b0905397b82a6b3ec6.jpg', '', 1, NULL, NULL, '2020-08-10 11:55:53', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (17, 0, 0, 'nav-icon-new.active.png', 3036, 'https://cdn.ckjia.com/5efe50fcd0e744eaa7a2e7c6d851dd82.png', '5efe50fcd0e744eaa7a2e7c6d851dd82.png', '', 1, NULL, NULL, '2020-08-10 13:39:06', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (18, 0, 0, 'nav-icon-user.active.png', 2150, 'https://cdn.ckjia.com/90cef6a278b34c1690af938193e2d813.png', '90cef6a278b34c1690af938193e2d813.png', '', 1, NULL, NULL, '2020-08-10 13:40:56', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (19, 0, 0, 'nav-icon-cat.active.png', 3338, 'https://cdn.ckjia.com/8ffa2bf6e6e7491b8460bf308abd30de.png', '8ffa2bf6e6e7491b8460bf308abd30de.png', '', 1, NULL, NULL, '2020-08-10 13:41:49', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (20, 0, 0, 'nav-icon-index.active.png', 2754, 'https://cdn.ckjia.com/478acfc9aeb140a4b79c6402ba3bd021.png', '478acfc9aeb140a4b79c6402ba3bd021.png', '', 1, NULL, NULL, '2020-08-10 13:54:53', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (21, 0, 0, 'baiduzhifu2x.png', 19415, 'https://cdn.ckjia.com/5ba794ec8d054ce995d37d364c5a9836.png', '5ba794ec8d054ce995d37d364c5a9836.png', '', 1, NULL, NULL, '2020-08-10 13:56:10', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (22, 0, 0, 'h_seckill.png', 6008, 'https://cdn.ckjia.com/897d70b0635f48999baa635d6debbbee.png', '897d70b0635f48999baa635d6debbbee.png', '', 1, NULL, NULL, '2020-08-10 13:59:47', NULL, '0', 0);
INSERT INTO `sys_attachment` (`id`, `storage_id`, `attachment_group_id`, `name`, `size`, `url`, `file_name`, `thumb_url`, `type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `is_recycle`) VALUES (24, 0, 0, 'flybg2.jpeg', 79028, 'https://cdn.ckjia.com/7667a4086d3c40948207dc8e02b52ff9.jpeg', '7667a4086d3c40948207dc8e02b52ff9.jpeg', '', 1, NULL, NULL, '2020-08-11 14:19:53', NULL, '0', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(48) NOT NULL COMMENT '客户端id',
  `client_secret` varchar(256) NOT NULL COMMENT '客户端密钥',
  `resource_ids` varchar(256) DEFAULT NULL COMMENT '资源集合',
  `scope` varchar(256) NOT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(256) NOT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(256) DEFAULT NULL COMMENT '权限',
  `access_token_validity` int(11) NOT NULL COMMENT '令牌过期秒数',
  `refresh_token_validity` int(11) NOT NULL COMMENT '刷新令牌过期秒数',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '附加说明',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '自动授权',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `is_deleted` int(2) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='客户端表';

-- ----------------------------
-- Records of sys_client
-- ----------------------------
BEGIN;
INSERT INTO `sys_client` (`id`, `client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_by`, `update_by`, `create_time`, `update_time`, `status`, `is_deleted`) VALUES (1, 'fly', 'fly_secret', NULL, 'all', 'refresh_token,password,authorization_code,captcha,sms,social', 'http://localhost:10001', NULL, 3600, 3600, NULL, NULL, NULL, NULL, '2020-07-12 14:49:23', '2020-07-28 04:22:54', '0', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父主键',
  `code` varchar(255) DEFAULT NULL COMMENT '码',
  `c_key` varchar(255) DEFAULT NULL COMMENT '值',
  `value` varchar(255) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `tenant_id` int(11) DEFAULT '1',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (1, 0, 'oss', 'default', 'qiniuoss', 0, '默认OSS配置', NULL, NULL, '2020-08-08 01:44:31', '2020-12-16 09:37:21', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (2, 1, 'alioss', 'endpoint', 'oss-cn-beijing.aliyuncs.com', 1, '对象存储服务的URL', NULL, NULL, '2020-08-08 01:46:10', '2020-08-09 16:14:15', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (3, 1, 'alioss', 'customDomain', 'mall-zaitong.oss-cn-beijing.aliyuncs.com', 2, '自定义域名', NULL, NULL, '2020-08-08 01:46:32', '2020-08-09 16:14:08', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (4, 1, 'alioss', 'pathStyleAccess', 'false', 3, 'pathStyleAccess', NULL, NULL, '2020-08-08 01:47:21', '2020-08-08 01:47:35', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (5, 1, 'alioss', 'accessKey', 'LTA******rzjrV', 4, 'Access Key', NULL, NULL, '2020-08-08 01:47:40', '2020-08-09 07:53:48', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (6, 1, 'alioss', 'secretKey', '9H6Bxg**************bfNoy4E', 5, 'Access Secret', NULL, NULL, '2020-08-08 01:53:13', '2020-08-10 01:31:53', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (7, 1, 'alioss', 'bucketName', 'm********g', 6, '空间名', NULL, NULL, '2020-08-08 01:53:14', '2020-08-09 16:13:15', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (8, 1, 'qiniuoss', 'endpoint', 's3-cn-south-1.qiniucs.com', 1, '对象存储服务的URL', NULL, NULL, '2020-08-08 01:46:10', '2020-08-10 01:33:31', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (9, 1, 'qiniuoss', 'customDomain', 'cd**********com8878556757657', 2, '自定义域名', NULL, NULL, '2020-08-08 01:46:32', '2020-11-15 20:02:32', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (10, 1, 'qiniuoss', 'pathStyleAccess', 'false', 3, 'pathStyleAccess', NULL, NULL, '2020-08-08 01:47:21', '2020-08-08 01:47:35', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (11, 1, 'qiniuoss', 'accessKey', 'pj2M-4k_*********************dQpjb1L', 4, 'Access Key', NULL, NULL, '2020-08-08 01:47:40', '2020-08-10 01:33:31', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (12, 1, 'qiniuoss', 'secretKey', 'Dx17O-dbR*******************Mxlc4bb', 5, 'Access Secret', NULL, NULL, '2020-08-08 01:53:13', '2020-08-10 01:33:31', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (13, 1, 'qiniuoss', 'bucketName', 'ckjia', 6, '空间名', NULL, NULL, '2020-08-08 01:53:14', '2020-08-10 01:33:31', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (14, 1, 'miniooss', 'endpoint', '66666', 1, '对象存储服务的URL', NULL, NULL, '2020-08-08 01:46:10', '2020-08-09 02:03:52', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (15, 1, 'miniooss', 'customDomain', '2222', 2, '自定义域名', NULL, NULL, '2020-08-08 01:46:32', '2020-08-08 16:55:54', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (16, 1, 'miniooss', 'pathStyleAccess', 'false', 3, 'pathStyleAccess', NULL, NULL, '2020-08-08 01:47:21', '2020-08-08 01:47:35', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (17, 1, 'miniooss', 'accessKey', '3333', 4, 'Access Key', NULL, NULL, '2020-08-08 01:47:40', '2020-08-08 16:55:58', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (18, 1, 'miniooss', 'secretKey', '4444', 5, 'Access Secret', NULL, NULL, '2020-08-08 01:53:13', '2020-08-08 16:56:02', 1, 0);
INSERT INTO `sys_config` (`id`, `parent_id`, `code`, `c_key`, `value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `tenant_id`, `is_deleted`) VALUES (19, 1, 'miniooss', 'bucketName', '5555', 6, '空间名', NULL, NULL, '2020-08-08 01:53:14', '2020-08-08 16:56:06', 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` char(1) DEFAULT '0' COMMENT '删除标识',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '上级ID',
  `tenant_id` bigint(20) DEFAULT '0' COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='组织机构表';

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
BEGIN;
INSERT INTO `sys_depart` (`id`, `name`, `sort`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `parent_id`, `tenant_id`) VALUES (1, '开发部', 0, NULL, NULL, '2020-06-27 15:30:50', '2020-07-01 20:49:08', '0', -1, 0);
INSERT INTO `sys_depart` (`id`, `name`, `sort`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `parent_id`, `tenant_id`) VALUES (2, '开发分部', 0, NULL, NULL, '2020-06-29 11:14:37', NULL, '0', 1, 0);
INSERT INTO `sys_depart` (`id`, `name`, `sort`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `parent_id`, `tenant_id`) VALUES (3, '开发二部', 1, NULL, NULL, '2020-06-29 15:54:27', NULL, '0', 1, 0);
INSERT INTO `sys_depart` (`id`, `name`, `sort`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `parent_id`, `tenant_id`) VALUES (4, '产品部', 1, NULL, NULL, '2020-06-29 07:58:54', '2020-08-17 06:53:59', '0', -1, 0);
INSERT INTO `sys_depart` (`id`, `name`, `sort`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `parent_id`, `tenant_id`) VALUES (5, '产品一部', 1, NULL, NULL, '2020-06-29 15:59:14', NULL, '0', 4, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父主键',
  `code` varchar(255) DEFAULT NULL COMMENT '字典码',
  `dict_key` varchar(255) DEFAULT NULL COMMENT '字典值',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '字典备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 0, 'status', '-1', '状态', 1, '', NULL, NULL, '2020-07-01 09:59:15', '2020-07-01 10:02:00', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 1, 'status', '0', '启用', 1, NULL, NULL, NULL, '2020-07-01 10:02:23', '2020-07-01 10:02:59', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 1, 'status', '1', '禁用', 2, NULL, NULL, NULL, '2020-07-01 10:02:34', '2020-07-01 10:02:59', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 0, 'dbtype', '-1', '数据库类型', 1, NULL, NULL, NULL, '2020-07-11 08:47:02', NULL, 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 4, 'dbtype', 'mysql', 'com.mysql.cj.jdbc.Driver', 1, NULL, NULL, NULL, '2020-07-11 08:47:44', '2020-07-11 08:53:11', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 4, 'dbtype', 'oracle', 'oracle.jdbc.OracleDriver', 2, NULL, NULL, NULL, '2020-07-11 08:48:00', '2020-07-11 08:54:05', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 4, 'dbtype', 'oracle12c', 'oracle.jdbc.OracleDriver', 3, NULL, NULL, NULL, '2020-07-11 08:49:10', '2020-07-11 08:54:12', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (24, 0, 'ok', '-1', '确认', NULL, NULL, NULL, NULL, '2020-07-19 13:31:16', '2020-07-19 21:31:28', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (25, 24, 'ok', 'yes', '是', NULL, NULL, NULL, NULL, '2020-07-19 21:31:40', '2020-07-20 05:32:12', 0);
INSERT INTO `sys_dict` (`id`, `parent_id`, `code`, `dict_key`, `dict_value`, `sort`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (26, 24, 'ok', 'no', '否', NULL, NULL, NULL, NULL, '2020-07-20 05:32:06', NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `trace_id` varchar(64) DEFAULT NULL COMMENT '跟踪ID',
  `title` varchar(64) DEFAULT NULL COMMENT '日志标题',
  `operation` text COMMENT '操作内容',
  `method` varchar(64) DEFAULT NULL COMMENT '执行方法',
  `params` text COMMENT '参数',
  `url` varchar(128) DEFAULT NULL COMMENT '请求路径',
  `ip` varchar(64) DEFAULT NULL COMMENT 'ip地址',
  `exception` text,
  `execute_time` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `location` varchar(64) DEFAULT NULL COMMENT '地区',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` char(1) DEFAULT '0' COMMENT '删除标识',
  `tenant_id` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `type` int(11) DEFAULT '0' COMMENT '菜单类型（sys_type）',
  `name` varchar(32) DEFAULT NULL COMMENT '菜单标题',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限',
  `button_permission` text COMMENT '按钮权限',
  `level` int(11) DEFAULT '1' COMMENT '菜单等级',
  `path` varchar(128) NOT NULL COMMENT '路由路径',
  `component` varchar(128) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) DEFAULT NULL COMMENT '菜单图标',
  `status` char(1) DEFAULT '0' COMMENT '状态（0:启用，1:禁用）',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `keep_alive` bit(1) DEFAULT b'0' COMMENT '是否缓存该页面: 1:是  0:不是',
  `hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否隐藏',
  `target` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否外链',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1830636401189285891 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 0, '系统管理', 0, 'sys', '[{\"btnName\":\"管理\",\"btnPermission\":\"manage\"}]', 1, '/sys', 'Layout', 'appstore-outlined', '0', 2, NULL, b'0', b'0', b'1', NULL, '23', '2020-06-17 14:21:45', '2024-09-03 10:24:03', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 0, '用户管理', 1, 'sys.user', '[{\"btnName\":\"列表\",\"btnPermission\":\"list\"},{\"btnName\":\"新增or修改\",\"btnPermission\":\"saveOrUpdate\"},{\"btnName\":\"禁用启用\",\"btnPermission\":\"enable\"},{\"btnName\":\"删除\",\"btnPermission\":\"delete\"}]', 2, '/sys/user', '/system/user/index', 'ios-contact', '0', 4, NULL, b'0', b'0', b'1', NULL, '2', '2020-06-18 14:28:36', '2024-09-03 10:24:03', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 0, '菜单管理', 1, 'sys.menu', '[{\"btnName\":\"列表\",\"btnPermission\":\"list\"},{\"btnName\":\"新增Or修改\",\"btnPermission\":\"saveOrUpdate\"},{\"btnName\":\"禁用启用\",\"btnPermission\":\"enable\"},{\"btnName\":\"删除\",\"btnPermission\":\"delete\"}]', 2, '/sys/menu', '/system/menu/index', 'md-menu', '0', 6, NULL, b'0', b'0', b'1', NULL, '2', '2020-06-18 14:28:36', '2024-09-03 10:24:03', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 0, '角色管理', 1, 'sys.role', '[{\"btnName\":\"列表\",\"btnPermission\":\"list\"},{\"btnName\":\"新增or删除\",\"btnPermission\":\"saveOrUpdate\"},{\"btnName\":\"禁用启用\",\"btnPermission\":\"enable\"},{\"btnName\":\"删除\",\"btnPermission\":\"delete\"}]', 2, '/sys/role', '/system/role/index', 'md-person', '0', 8, NULL, b'0', b'0', b'1', NULL, '2', '2020-06-18 14:28:36', '2024-09-03 10:24:03', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (2070, 0, '文件服务器管理', 0, 'file', '[{\"btnName\":\"连接ftp\",\"btnPermission\":\"connect\"},{\"btnName\":\"新增文件夹\",\"btnPermission\":\"create\"},{\"btnName\":\"列表\",\"btnPermission\":\"list\"},{\"btnName\":\"上传\",\"btnPermission\":\"upload\"},{\"btnName\":\"下载\",\"btnPermission\":\"download\"},{\"btnName\":\"删除\",\"btnPermission\":\"delete\"}]', 1, '/home', 'Layout', 'md-folder', '0', 0, '123', b'0', b'0', b'1', NULL, '23', '2020-06-17 14:21:45', '2024-09-03 10:20:26', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1829910809267490818, 0, 'test', 0, 'test', '[{\"btnName\":\"管理\",\"btnPermission\":\"manage\"}]', 1, '/test', '', '', '0', 8, 'ets', b'0', b'0', b'0', '23', '2', '2024-08-31 23:55:10', '2024-09-02 23:57:33', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1829918085478899713, 0, 'test2', 1829910809267490818, 'test2', '[{\"btnName\":\"列表\",\"btnPermission\":\"list\"}]', 2, '/sys/test', '', '', '0', 1, '', b'0', b'0', b'0', '23', '2', '2024-09-01 00:24:04', '2024-09-02 23:13:48', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1830636113204178945, 0, '测试demo', 0, 'demo', '[{\"btnName\":\"管理\",\"btnPermission\":\"manage\"}]', 1, 'demo', '', '', '1', 9, '', b'0', b'0', b'0', '2', '2', '2024-09-02 23:57:16', '2024-09-02 23:59:25', b'0');
INSERT INTO `sys_menu` (`id`, `type`, `name`, `parent_id`, `permission`, `button_permission`, `level`, `path`, `component`, `icon`, `status`, `sort`, `remark`, `keep_alive`, `hidden`, `target`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1830636401189285890, 0, '测试2', 1830636113204178945, 'demo.test', '[{\"btnName\":\"列表\",\"btnPermission\":\"list\"}]', 2, 'demo/test', '', '', '1', 2, '', b'0', b'0', b'0', '2', '2', '2024-09-02 23:58:24', '2024-09-02 23:59:08', b'0');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` int(11) DEFAULT '0' COMMENT '角色类型（sys_type）',
  `name` varchar(64) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(64) DEFAULT NULL COMMENT '角色编码',
  `remark` varchar(256) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` char(1) DEFAULT NULL COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_role_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1830787389479833603 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `type`, `name`, `code`, `remark`, `sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES (1, 0, '超级管理员', 'admin', '管理员组', 0, '0', NULL, '2020-06-28 15:02:16', '1830639207036055553', '2024-09-03 09:59:51', b'0');
INSERT INTO `sys_role` (`id`, `type`, `name`, `code`, `remark`, `sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES (2, 0, '测试', 'demo2', '演示会员组', 9, '1', NULL, '2020-06-28 07:02:36', '2', '2024-09-02 23:56:35', b'0');
INSERT INTO `sys_role` (`id`, `type`, `name`, `code`, `remark`, `sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES (1830131679634132993, 0, '测试角色', 'test', 'testtest', 9, '0', '23', '2024-09-01 14:32:49', '2', '2024-09-02 23:44:55', b'0');
INSERT INTO `sys_role` (`id`, `type`, `name`, `code`, `remark`, `sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES (1830633472852074498, 0, '基础管理员', 'base', '可以查看基础信息', 2, '0', '2', '2024-09-02 23:46:46', NULL, NULL, b'0');
INSERT INTO `sys_role` (`id`, `type`, `name`, `code`, `remark`, `sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`) VALUES (1830787389479833602, 0, '按钮测试', 'test', '', 15, '0', '2', '2024-09-03 09:58:23', NULL, NULL, b'0');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(64) DEFAULT NULL COMMENT '菜单id',
  `permission` varchar(256) DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_role_menu_role_id_index` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1830787758566002703 DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单权限表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633009675083777, 1830131679634132993, 2070, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633009683472385, 1830131679634132993, 1, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633009683472386, 1830131679634132993, 1829918085478899713, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633009683472387, 1830131679634132993, 1829910809267490818, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473384751105, 1830633472852074498, 2070, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473384751106, 1830633472852074498, 2070, 'connect');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473388945409, 1830633472852074498, 2, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473388945410, 1830633472852074498, 3, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473388945411, 1830633472852074498, 4, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473388945412, 1830633472852074498, 1, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473388945413, 1830633472852074498, 1829910809267490818, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830633473388945414, 1830633472852074498, 1829918085478899713, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942185627650, 2, 2070, 'connect');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942198210562, 2, 2070, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942198210563, 2, 1, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942198210564, 2, 2, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942202404866, 2, 3, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942202404867, 2, 4, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942206599169, 2, 1829910809267490818, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830635942206599170, 2, 1829918085478899713, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389718908930, 1830787389479833602, 2070, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389718908931, 1830787389479833602, 1, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389718908932, 1830787389479833602, 2, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389718908933, 1830787389479833602, 3, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389718908934, 1830787389479833602, 4, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389731491841, 1830787389479833602, 1829910809267490818, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787389731491842, 1830787389479833602, 1829918085478899713, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758540836866, 1, 2070, 'connect');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758553419777, 1, 2070, 'create');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758553419778, 1, 2070, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758553419779, 1, 2070, 'upload');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758553419780, 1, 2070, 'download');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758553419781, 1, 2070, 'delete');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758553419782, 1, 1, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002689, 1, 2, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002690, 1, 2, 'saveOrUpdate');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002691, 1, 2, 'enable');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002692, 1, 2, 'delete');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002693, 1, 3, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002694, 1, 3, 'saveOrUpdate');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002695, 1, 3, 'enable');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002696, 1, 3, 'delete');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002697, 1, 4, 'list');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002698, 1, 4, 'saveOrUpdate');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002699, 1, 4, 'enable');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002700, 1, 4, 'delete');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002701, 1, 1829910809267490818, 'manage');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `permission`) VALUES (1830787758566002702, 1, 1829918085478899713, 'list');
COMMIT;

-- ----------------------------
-- Table structure for sys_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_route`;
CREATE TABLE `sys_route` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) DEFAULT NULL COMMENT '服务名称',
  `path` varchar(255) DEFAULT NULL COMMENT '服务前缀',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  `service_id` varchar(100) DEFAULT NULL COMMENT '服务编码',
  `status` char(1) DEFAULT '0' COMMENT 'API状态:0:启用 1:禁用',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` char(1) DEFAULT '0' COMMENT '删除标识',
  `tenant_id` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='系统路由表';

-- ----------------------------
-- Records of sys_route
-- ----------------------------
BEGIN;
INSERT INTO `sys_route` (`id`, `name`, `path`, `url`, `service_id`, `status`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tenant_id`) VALUES (1, '系统服务', '/flysystem/**', 'flysystem', 'flysystem', '0', NULL, NULL, '2020-10-18 22:59:02', '2021-09-23 14:13:21', '0', NULL);
INSERT INTO `sys_route` (`id`, `name`, `path`, `url`, `service_id`, `status`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tenant_id`) VALUES (2, '认证服务', '/flyuaa/**', 'flyuaa', 'flyuaa', '0', NULL, NULL, '2020-10-18 15:14:13', '2021-09-23 14:13:36', '0', NULL);
INSERT INTO `sys_route` (`id`, `name`, `path`, `url`, `service_id`, `status`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tenant_id`) VALUES (3, '代码服务', '/flycode/**', 'flycode', 'flycode', '0', NULL, NULL, '2020-10-18 20:21:25', '2021-09-23 14:13:31', '0', NULL);
INSERT INTO `sys_route` (`id`, `name`, `path`, `url`, `service_id`, `status`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`, `tenant_id`) VALUES (4, '组件服务', '/flycomponent/**', 'flycomponent', 'flycomponent', '0', NULL, NULL, '2020-10-18 20:22:42', '2021-09-23 14:13:27', '0', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_type`;
CREATE TABLE `sys_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '系统类型（0:平台系统，1:商家系统，2:音乐平台系统，3:音乐人系统等）',
  `name` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '系统类型名称',
  `remark` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '系统类型描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='系统类型表（划分为用户、菜单、角色等）';

-- ----------------------------
-- Records of sys_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_type` (`id`, `type`, `name`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, 0, '平台管理系统', NULL, NULL, NULL, '2024-08-31 04:31:07', '2024-08-31 09:04:38');
INSERT INTO `sys_type` (`id`, `type`, `name`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, 1, '商家管理系统', NULL, NULL, NULL, '2024-08-31 04:31:07', '2024-08-31 09:04:38');
INSERT INTO `sys_type` (`id`, `type`, `name`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (3, 2, '音乐平台管理系统', NULL, NULL, NULL, '2024-08-31 04:31:07', '2024-08-31 09:04:38');
INSERT INTO `sys_type` (`id`, `type`, `name`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (4, 3, '音乐歌手管理系统', NULL, NULL, NULL, '2024-08-31 04:31:07', '2024-08-31 09:06:13');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(45) DEFAULT NULL COMMENT '账号',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `user_type` int(11) DEFAULT '0' COMMENT '用户类型（sys_type）',
  `login_type` int(11) DEFAULT '0' COMMENT '登录类型（1：用户名密码登录　2：手机号登录　3：社交登录）',
  `name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(10) DEFAULT NULL COMMENT '真名',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(45) DEFAULT NULL COMMENT '手机',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` smallint(6) DEFAULT NULL COMMENT '性别',
  `depart_id` bigint(20) DEFAULT '0' COMMENT '部门id',
  `status` char(1) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1830639207036055554 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 'admin', '{bcrypt}$2a$10$afoLtVN.wky4hxwEUvmGWeg49aoHfLxiHeDYVlTXQXPpYh94R0RU2', 0, 0, 'admin', '超级管理员', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18810001000', NULL, 1, 1, '0', NULL, NULL, '23', '2020-07-02 07:29:12', '2024-09-01 20:21:23', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 'test2', '{bcrypt}$2a$10$NqoeivyGXGPzQUGMkDmo3O5f6fNtiUk4Ae76rmyGvvGMZbzAqlGoS', 0, 0, 'test2', '杨幂', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 4, '0', '123', NULL, '2', '2020-06-16 20:05:43', '2024-09-02 23:18:02', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 'admin4', '{bcrypt}$2a$10$UIrEMyC0GUIUdJuGPYeGO.Nc8AZYlTiC8MUttPaYQ7P.V5q/cTAAG', 0, 0, 'admin4', '刘德华', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '0', NULL, NULL, '2', '2020-06-17 04:05:43', '2024-09-02 23:18:13', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 'admin5', '{bcrypt}$2a$10$WF2dEcJetVqRFOn/AfrfZu4.Z5HcBFZ49/fL/KKOQ6b.xJtXZHoqe', 0, 0, 'admin5', 'fly', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '0', NULL, NULL, '23', '2020-06-17 04:05:43', '2024-09-01 20:21:05', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 'admin6', '{bcrypt}$2a$10$p1SVo5ne.nswCVIAhB2UgOlyAqEoBFlHZ7/aUUj.H/XD0qRd1Osly', 0, 0, 'admin66', 'fly', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '0', NULL, NULL, '2', '2020-06-17 12:05:43', '2024-09-02 23:31:20', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 'admin7', '{bcrypt}$2a$10$pDzXQpiSIl74Jekj9CxMWOPbEV9MHkjkZ7GXX/RomtIyXz8m6Ruia', 0, 0, 'admin6', 'fly', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '0', NULL, NULL, NULL, '2020-06-16 12:05:43', '2020-06-30 12:55:52', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (8, 'admin8', '{bcrypt}$2a$10$6YPJ0dkaqh9x3zwTfidRBeT0U5vJ7Si7wXFx9Gc/K3pMxz8J18TrW', 0, 0, 'admin6', 'fly', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '0', NULL, NULL, NULL, '2020-06-16 20:05:43', '2020-07-15 04:31:24', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (9, 'admin9', '{bcrypt}$2a$10$pDzXQpiSIl74Jekj9CxMWOPbEV9MHkjkZ7GXX/RomtIyXz8m6Ruia', 0, 0, 'admin6', 'fly', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '1', NULL, NULL, '23', '2020-06-16 20:05:43', '2024-09-01 20:01:56', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (10, 'admin10', '{bcrypt}$2a$10$A5cfRbFDCsOg.1UTlWyEZu8DJHSr9GnANXQMsRSIDAtZHuiQicr0K', 0, 0, 'admin6', '测试管理员', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18910001000', NULL, 1, 1, '1', '测试管理员', NULL, NULL, '2020-06-15 12:05:43', '2021-08-29 15:57:25', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (22, 'pp1', '{bcrypt}$2a$10$jddK.xwTn99XM9ggy64Zgu.eBK2GBiyk9RmtQEjg99S2F8otQ8ieq', 0, 0, 'pp1', '11', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', 'pp1@163.com', '1899', '2020-06-26 16:00:00', 2, 1, '1', NULL, NULL, '23', '2020-06-30 09:57:13', '2024-09-01 20:02:00', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (23, 'adminfile', '{bcrypt}$2a$10$K4moF8QXtcimrBYtbNglwOR489mq1Ask4RIotv2d6a6LkBGBseVYa', 0, 0, 'adminfile', '超级管理员', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2084118128,2518711034&fm=26&gp=0.jpg', '2570078967@qq.com', '18810001000', NULL, 1, 1, '0', NULL, NULL, '2', '2024-08-14 07:29:12', '2024-09-02 23:17:07', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1830258070073729026, 'test', '{bcrypt}$2a$10$nPHmi1nfSiAWN4HdNd2i2.7LVTg6EvUDITWIae7P2kVONc/rOfcqe', 0, 0, 'test', NULL, NULL, NULL, '15521142480', NULL, NULL, 0, '0', '2', '23', '2', '2024-09-01 22:55:03', '2024-09-03 09:58:36', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1830270155591622657, 'test3', '{bcrypt}$2a$10$1aOcVIvp2CqIeaC4XHUHsuespybRpRIrlkUXm26sCNUMvl/a28A9G', 0, 0, 'test4', NULL, NULL, NULL, NULL, NULL, NULL, 0, '0', '123456', '23', '2', '2024-09-01 23:43:05', '2024-09-02 23:17:25', '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1830273797430288386, 'test4', '{bcrypt}$2a$10$vlutbObOUxdQR/m6Rwg7tOaXPfbgZsRfumarWpMA0SWhIcMPmPSJm', 0, 0, 'test3', NULL, NULL, NULL, NULL, NULL, NULL, 0, '1', NULL, '23', NULL, '2024-09-01 23:57:33', NULL, '0');
INSERT INTO `sys_user` (`id`, `account`, `password`, `user_type`, `login_type`, `name`, `real_name`, `avatar`, `email`, `telephone`, `birthday`, `sex`, `depart_id`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1830639207036055553, 'lxs', '{bcrypt}$2a$10$b8x61RXapOWIXMPXiYpxKOlhENVY9EnsuwL5s.Cub7E52Ws0blowu', 0, 0, 'lxs', NULL, NULL, NULL, NULL, NULL, NULL, 0, '0', NULL, '2', NULL, '2024-09-03 00:09:33', NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(64) DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `sys_user_role_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1830787443502469122 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1, 2, 1);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830273797866496002, 1830273797430288386, 2);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830626013030752257, 23, 1);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830626086464626690, 1830270155591622657, 1830131679634132993);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830626241783898114, 3, 1830131679634132993);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830626287975768066, 4, 2);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830629589769625601, 6, 1830131679634132993);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830639207501623297, 1830639207036055553, 1);
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1830787443502469121, 1830258070073729026, 1830787389479833602);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
