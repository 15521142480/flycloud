-- flycloud-ai 统一聊天与 Chat Memory 基础表。
-- 该脚本同时包含当前已经落地的知识库、文档与 Chunk 表。Agent 和 MCP 当前为代码固定的受控能力，
-- 尚无运营侧动态配置需求，因此不创建没有实际读写方的空配置表。

CREATE TABLE IF NOT EXISTS `ai_conversation` (
    `id` char(36) NOT NULL COMMENT 'UUID 会话编号',
    `user_id` bigint NOT NULL COMMENT '所属用户编号',
    `title` varchar(120) NOT NULL COMMENT '会话展示标题',
    `last_message_time` datetime NOT NULL COMMENT '最后消息时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0 否，1 是',
    PRIMARY KEY (`id`),
    KEY `idx_ai_conversation_user_last` (`user_id`, `is_deleted`, `last_message_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 统一聊天会话';

CREATE TABLE IF NOT EXISTS `ai_message` (
    `id` char(36) NOT NULL COMMENT 'UUID 消息编号',
    `conversation_id` char(36) NOT NULL COMMENT '所属会话编号',
    `user_id` bigint NOT NULL COMMENT '所属用户编号',
    `role` varchar(16) NOT NULL COMMENT '消息角色：user、assistant、system、tool',
    `message_type` varchar(32) NOT NULL COMMENT '消息类型：text、tool_call、tool_result',
    `content` mediumtext COMMENT '消息文本或工具结果',
    `model_provider` varchar(32) DEFAULT NULL COMMENT '实际模型供应商',
    `model_name` varchar(100) DEFAULT NULL COMMENT '实际模型名称',
    `input_tokens` bigint DEFAULT NULL COMMENT '输入 Token',
    `output_tokens` bigint DEFAULT NULL COMMENT '输出 Token',
    `total_tokens` bigint DEFAULT NULL COMMENT '总 Token',
    `status` varchar(16) NOT NULL COMMENT '消息状态：generating、completed、failed',
    `metadata` json DEFAULT NULL COMMENT '工具权限、工具名及后续 RAG/Agent/MCP 扩展元数据',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0 否，1 是',
    PRIMARY KEY (`id`),
    KEY `idx_ai_message_conversation_time` (`conversation_id`, `is_deleted`, `create_time`),
    KEY `idx_ai_message_user_time` (`user_id`, `is_deleted`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 统一聊天消息';

CREATE TABLE IF NOT EXISTS `ai_knowledge_base` (
    `id` char(36) NOT NULL COMMENT 'UUID 知识库编号',
    `name` varchar(100) NOT NULL COMMENT '知识库名称',
    `description` varchar(500) DEFAULT NULL COMMENT '知识库说明',
    `status` varchar(16) NOT NULL DEFAULT 'enabled' COMMENT '状态：enabled、disabled',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0 否，1 是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ai_knowledge_base_name` (`name`, `is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 知识库';

CREATE TABLE IF NOT EXISTS `ai_knowledge_document` (
    `id` char(36) NOT NULL COMMENT 'UUID 文档编号',
    `knowledge_base_id` char(36) NOT NULL COMMENT '所属知识库编号',
    `title` varchar(200) NOT NULL COMMENT '文档标题',
    `source_type` varchar(32) NOT NULL COMMENT '来源类型：manual、file、url',
    `source_uri` varchar(500) DEFAULT NULL COMMENT '原始文件或地址',
    `content_hash` char(64) DEFAULT NULL COMMENT '正文 SHA-256，用于去重与重建判断',
    `status` varchar(16) NOT NULL DEFAULT 'completed' COMMENT '状态：pending、processing、completed、failed',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0 否，1 是',
    PRIMARY KEY (`id`),
    KEY `idx_ai_knowledge_document_base_status` (`knowledge_base_id`, `is_deleted`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 知识库文档';

CREATE TABLE IF NOT EXISTS `ai_knowledge_chunk` (
    `id` char(36) NOT NULL COMMENT 'UUID Chunk 编号',
    `document_id` char(36) NOT NULL COMMENT '所属文档编号',
    `chunk_index` int NOT NULL COMMENT '文档内从 0 开始的分片序号',
    `content` mediumtext NOT NULL COMMENT '用于 Embedding 的文本内容',
    `vector_store_id` char(36) NOT NULL COMMENT 'Qdrant Point/Document 编号',
    `metadata` json DEFAULT NULL COMMENT '检索过滤与展示元数据',
    `status` varchar(16) NOT NULL DEFAULT 'completed' COMMENT '状态：pending、processing、completed、failed',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0 否，1 是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ai_knowledge_chunk_document_index` (`document_id`, `chunk_index`, `is_deleted`),
    UNIQUE KEY `uk_ai_knowledge_chunk_vector` (`vector_store_id`, `is_deleted`),
    KEY `idx_ai_knowledge_chunk_document` (`document_id`, `is_deleted`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 知识库文档分片';

-- 第 7 步 RAG 的可观察初始化知识：MySQL 为知识来源与审计索引，应用启动后将同一批固定 Chunk
-- 真实向量化后 upsert 到 Qdrant。后续上传文件只需继续写入这三张表并复用现有向量服务。
INSERT IGNORE INTO `ai_knowledge_base` (`id`, `name`, `description`, `status`)
VALUES ('c6ca2f14-50e7-4d64-9897-4a1d6d0cb401', '飞翔云初始化测试知识库', 'RAG 第一版用于观察完整检索链路的测试知识', 'enabled');

INSERT IGNORE INTO `ai_knowledge_document` (`id`, `knowledge_base_id`, `title`, `source_type`, `status`)
VALUES
('d07c0fa7-ea32-4f3d-a25f-5d95a8ab6101', 'c6ca2f14-50e7-4d64-9897-4a1d6d0cb401', '商城退款规定', 'manual', 'completed'),
('d07c0fa7-ea32-4f3d-a25f-5d95a8ab6102', 'c6ca2f14-50e7-4d64-9897-4a1d6d0cb401', '订单查询规则', 'manual', 'completed'),
('d07c0fa7-ea32-4f3d-a25f-5d95a8ab6103', 'c6ca2f14-50e7-4d64-9897-4a1d6d0cb401', '系统用户查询规则', 'manual', 'completed');

INSERT IGNORE INTO `ai_knowledge_chunk` (`id`, `document_id`, `chunk_index`, `content`, `vector_store_id`, `metadata`, `status`)
VALUES
('e1bf0e3c-716c-4d69-8bb5-58f5e7ec7101', 'd07c0fa7-ea32-4f3d-a25f-5d95a8ab6101', 0, '飞翔云商城退款规定：未发货订单可由订单创建人申请退款；已付款但未发货的订单可以退款。', 'e1bf0e3c-716c-4d69-8bb5-58f5e7ec7101', JSON_OBJECT('knowledgeBase', '商城规则', 'source', '初始化测试知识', 'topic', '退款'), 'completed'),
('e1bf0e3c-716c-4d69-8bb5-58f5e7ec7102', 'd07c0fa7-ea32-4f3d-a25f-5d95a8ab6101', 1, '飞翔云商城退款规定：已发货订单需先完成退货物流登记，平台审核通过后原路退款。', 'e1bf0e3c-716c-4d69-8bb5-58f5e7ec7102', JSON_OBJECT('knowledgeBase', '商城规则', 'source', '初始化测试知识', 'topic', '退款'), 'completed'),
('e1bf0e3c-716c-4d69-8bb5-58f5e7ec7103', 'd07c0fa7-ea32-4f3d-a25f-5d95a8ab6102', 0, '订单查询规则：超级管理员可查询全部订单；普通用户只能查询本人创建的订单。', 'e1bf0e3c-716c-4d69-8bb5-58f5e7ec7103', JSON_OBJECT('knowledgeBase', '商城规则', 'source', '初始化测试知识', 'topic', '订单权限'), 'completed'),
('e1bf0e3c-716c-4d69-8bb5-58f5e7ec7104', 'd07c0fa7-ea32-4f3d-a25f-5d95a8ab6103', 0, '系统用户查询工具仅返回公共用户资料，不返回密码、手机号、邮箱等敏感信息。', 'e1bf0e3c-716c-4d69-8bb5-58f5e7ec7104', JSON_OBJECT('knowledgeBase', '系统规则', 'source', '初始化测试知识', 'topic', '用户权限'), 'completed');
