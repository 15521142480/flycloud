package com.fly.ai.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 统一 AI 聊天消息。
 * <p>
 * {@code metadata} 保存可扩展的结构化消息附加信息，例如工具授权结果、工具名、后续 RAG 引用和 Agent/MCP 轨迹；
 * 不为尚未实现的能力提前增加大量专用列。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_message")
public class AiMessage extends BaseEntity {

    /** 消息 UUID。 */
    @TableId(type = IdType.INPUT)
    private String id;

    /** 所属统一会话标识。 */
    private String conversationId;

    /** 会话所属用户编号，用于审计和按用户统计。 */
    private Long userId;

    /** 消息角色：user、assistant、system、tool。 */
    private String role;

    /** 消息类型：text、tool_call、tool_result。 */
    private String messageType;

    /** 用户、模型或工具产生的文本内容。 */
    private String content;

    /** 本消息实际使用的模型供应商；用户消息为空。 */
    private String modelProvider;

    /** 本消息实际使用的模型名称；用户消息为空。 */
    private String modelName;

    /** 模型输入 Token，用于会话统计。 */
    private Long inputTokens;

    /** 模型输出 Token，用于会话统计。 */
    private Long outputTokens;

    /** 模型总 Token，用于会话统计。 */
    private Long totalTokens;

    /** 消息状态：completed、failed。 */
    private String status;

    /** JSON 格式的可扩展元数据。 */
    private String metadata;
}
