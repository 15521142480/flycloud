package com.fly.ai.chat.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.chat.domain.AiConversation;
import com.fly.ai.chat.domain.AiMessage;
import com.fly.ai.chat.domain.AiMessageRole;
import com.fly.ai.chat.domain.AiMessageStatus;
import com.fly.ai.chat.domain.AiMessageType;
import com.fly.ai.chat.mapper.AiConversationMapper;
import com.fly.ai.chat.mapper.AiMessageMapper;
import com.fly.ai.chat.memory.AiRedisChatMemoryRepository;
import com.fly.ai.chat.model.AiChatHistoryMessage;
import com.fly.ai.chat.model.AiConversationSummary;
import com.fly.ai.chat.model.AiMessageMetadata;
import com.fly.ai.common.model.AiPermission;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.model.AiUsage;
import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.tool.model.AiToolCallingResponse;
import com.fly.common.exception.AiProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 统一 AI 会话与完整消息历史服务。
 * <p>
 * MySQL 保存可展示、可审计的完整历史；本服务只在删除会话时清理 Redis，模型短期上下文的读写仍由
 * Spring AI {@code ChatMemory} 完成，避免混淆两种存储职责。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Service
@RequiredArgsConstructor
public class AiConversationService {

    private static final String DEFAULT_TITLE = "新会话";

    private final AiConversationMapper conversationMapper;

    private final AiMessageMapper messageMapper;

    private final AiRedisChatMemoryRepository chatMemoryRepository;

    private final ObjectMapper objectMapper;

    /**
     * 创建或校验会话，并先持久化本轮用户消息。
     * <p>
     * 助手消息必须在模型调用完成或失败后再写入，使消息创建时间就是实际发生时间，避免预创建占位消息
     * 造成历史顺序歧义。
     *
     * @param requestedConversationId 客户端携带的会话编号，可为空
     * @param userId 当前登录用户编号
     * @param content 用户输入
     * @return 本轮所属会话编号
     */
    @Transactional(rollbackFor = Exception.class)
    public String prepareTurn(String requestedConversationId, Long userId, String content) {
        AiConversation conversation = resolveConversation(requestedConversationId, userId);
        LocalDateTime now = LocalDateTime.now();
        if (DEFAULT_TITLE.equals(conversation.getTitle())) {
            conversation.setTitle(buildTitle(content));
        }
        conversation.setLastMessageTime(now);
        conversationMapper.updateById(conversation);

        AiMessage userMessage = new AiMessage();
        userMessage.setId(UUID.randomUUID().toString());
        userMessage.setConversationId(conversation.getId());
        userMessage.setUserId(userId);
        userMessage.setRole(AiMessageRole.USER.getValue());
        userMessage.setMessageType(AiMessageType.TEXT.getValue());
        userMessage.setContent(content);
        userMessage.setStatus(AiMessageStatus.COMPLETED.getValue());
        messageMapper.insert(userMessage);

        return conversation.getId();
    }

    /**
     * 在模型调用完成后持久化助手消息及其模型、Token、工具授权和知识库检索审计信息。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     * @param response 模型响应
     * @param provider 模型供应商标识
     * @param knowledgeReferences 本次实际命中的知识库片段
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssistantMessage(String conversationId, Long userId, AiToolCallingResponse response, String provider,
            List<AiKnowledgeHit> knowledgeReferences) {
        saveAssistantMessage(conversationId, userId,
                new AiChatResponse(response.responseId(), response.model(), response.content(), response.usage()), provider,
                response.permission(), response.toolNames(), knowledgeReferences);
    }

    /**
     * 在模型调用完成后持久化不包含工具调用的助手消息及其模型、Token 和 RAG 审计信息。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     * @param response 模型响应
     * @param provider 模型供应商标识
     * @param knowledgeReferences 本次实际命中的知识库片段
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAssistantMessage(String conversationId, Long userId, AiChatResponse response, String provider,
            List<AiKnowledgeHit> knowledgeReferences) {
        saveAssistantMessage(conversationId, userId, response, provider, null, Collections.emptyList(), knowledgeReferences);
    }

    /**
     * 持久化任意正式模型回答的公共实现。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     * @param response 模型响应
     * @param provider 模型供应商标识
     * @param permission 工具调用权限结果
     * @param toolNames 实际调用工具名称
     * @param knowledgeReferences 本次实际命中的知识库片段
     */
    private void saveAssistantMessage(String conversationId, Long userId, AiChatResponse response, String provider,
            AiPermission permission, List<String> toolNames, List<AiKnowledgeHit> knowledgeReferences) {
        AiUsage usage = response.usage();
        AiMessage message = new AiMessage();
        message.setId(UUID.randomUUID().toString());
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setRole(AiMessageRole.ASSISTANT.getValue());
        message.setMessageType(AiMessageType.TEXT.getValue());
        message.setContent(response.content());
        message.setModelProvider(provider);
        message.setModelName(response.model());
        message.setInputTokens(usage == null ? null : usage.inputTokens());
        message.setOutputTokens(usage == null ? null : usage.outputTokens());
        message.setTotalTokens(usage == null ? null : usage.totalTokens());
        message.setMetadata(writeMetadata(permission, toolNames, knowledgeReferences));
        message.setStatus(AiMessageStatus.COMPLETED.getValue());
        messageMapper.insert(message);
    }

    /**
     * 在模型调用失败后持久化助手失败消息，不写入底层异常细节，避免泄露内部信息。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFailedAssistantMessage(String conversationId, Long userId) {
        AiMessage message = new AiMessage();
        message.setId(UUID.randomUUID().toString());
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setRole(AiMessageRole.ASSISTANT.getValue());
        message.setMessageType(AiMessageType.TEXT.getValue());
        message.setContent("模型调用失败，请稍后重试");
        message.setStatus(AiMessageStatus.FAILED.getValue());
        messageMapper.insert(message);
    }

    /**
     * 查询当前用户的会话列表。
     *
     * @param userId 当前登录用户编号
     * @return 会话摘要列表
     */
    public List<AiConversationSummary> listConversations(Long userId) {
        return conversationMapper.selectList(Wrappers.<AiConversation>lambdaQuery()
                        .eq(AiConversation::getUserId, userId)
                        .orderByDesc(AiConversation::getLastMessageTime)
                        .orderByDesc(AiConversation::getCreateTime))
                .stream()
                .map(conversation -> new AiConversationSummary(conversation.getId(), conversation.getTitle(),
                        conversation.getLastMessageTime(), conversation.getCreateTime()))
                .toList();
    }

    /**
     * 查询属于当前用户的完整历史消息。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     * @return 历史消息列表
     */
    public List<AiChatHistoryMessage> listMessages(String conversationId, Long userId) {
        requireOwnedConversation(conversationId, userId);
        return messageMapper.selectList(Wrappers.<AiMessage>lambdaQuery()
                        .eq(AiMessage::getConversationId, conversationId)
                        .orderByAsc(AiMessage::getCreateTime)
                        .orderByAsc(AiMessage::getUpdateTime)
                        // 仅兼容历史秒级数据的并列排序；新消息使用毫秒时间，实际写入顺序自然唯一。
                        .orderByDesc(AiMessage::getRole))
                .stream()
                .map(this::toHistoryMessage)
                .toList();
    }

    /**
     * 逻辑删除当前用户的一段会话和其消息，同时清理 Redis 短期上下文。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteConversation(String conversationId, Long userId) {
        AiConversation conversation = requireOwnedConversation(conversationId, userId);
        messageMapper.delete(Wrappers.<AiMessage>lambdaQuery().eq(AiMessage::getConversationId, conversationId));
        conversationMapper.deleteById(conversation.getId());
        chatMemoryRepository.deleteByConversationId(conversationId);
    }

    /**
     * 重命名当前用户的一段会话。
     * <p>
     * 重命名属于会话元数据修改，不更新时间线字段，避免仅修改标题就改变会话在最近列表中的顺序。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     * @param title 新名称
     */
    @Transactional(rollbackFor = Exception.class)
    public void renameConversation(String conversationId, Long userId, String title) {
        AiConversation conversation = requireOwnedConversation(conversationId, userId);
        String normalizedTitle = title.trim();
        if (normalizedTitle.isEmpty()) {
            throw new AiProviderException(400, "会话名称不能为空");
        }
        conversation.setTitle(normalizedTitle);
        conversationMapper.updateById(conversation);
    }

    /**
     * 创建新会话或校验客户端指定的会话归属。
     *
     * @param requestedConversationId 客户端会话编号
     * @param userId 当前登录用户编号
     * @return 可使用的会话
     */
    private AiConversation resolveConversation(String requestedConversationId, Long userId) {
        if (requestedConversationId != null && !requestedConversationId.isBlank()) {
            return requireOwnedConversation(requestedConversationId, userId);
        }
        AiConversation conversation = new AiConversation();
        conversation.setId(UUID.randomUUID().toString());
        conversation.setUserId(userId);
        conversation.setTitle(DEFAULT_TITLE);
        conversation.setLastMessageTime(LocalDateTime.now());
        conversationMapper.insert(conversation);
        return conversation;
    }

    /**
     * 校验会话存在且只属于当前登录用户。
     *
     * @param conversationId 会话编号
     * @param userId 当前登录用户编号
     * @return 已校验的会话
     */
    private AiConversation requireOwnedConversation(String conversationId, Long userId) {
        AiConversation conversation = conversationMapper.selectOne(Wrappers.<AiConversation>lambdaQuery()
                .eq(AiConversation::getId, conversationId)
                .eq(AiConversation::getUserId, userId));
        if (conversation == null) {
            throw new AiProviderException(404, "AI 会话不存在或无权访问");
        }
        return conversation;
    }

    /**
     * 将消息转换为前端历史记录。
     *
     * @param message 消息实体
     * @return 历史消息
     */
    private AiChatHistoryMessage toHistoryMessage(AiMessage message) {
        AiMessageMetadata metadata = readMetadata(message.getMetadata());
        AiUsage usage = message.getTotalTokens() == null ? null : new AiUsage(valueOrZero(message.getInputTokens()),
                valueOrZero(message.getOutputTokens()), message.getTotalTokens());
        return new AiChatHistoryMessage(message.getId(), message.getRole(), message.getContent(), usage,
                metadata.permission(), metadata.toolNames(), metadata.knowledgeReferences(), message.getStatus(),
                message.getCreateTime());
    }

    /**
     * 构建会话首条标题。
     *
     * @param content 用户消息
     * @return 短标题
     */
    private String buildTitle(String content) {
        String title = content.replaceAll("\\s+", " ").trim();
        return title.length() > 40 ? title.substring(0, 40) + "…" : title;
    }

    /**
     * 序列化当前已实现的工具调用附加信息。
     *
     * @param permission 权限结果
     * @param toolNames 工具名称
     * @return JSON 元数据
     */
    private String writeMetadata(AiPermission permission, List<String> toolNames, List<AiKnowledgeHit> knowledgeReferences) {
        if (permission == null && (toolNames == null || toolNames.isEmpty())
                && (knowledgeReferences == null || knowledgeReferences.isEmpty())) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(new AiMessageMetadata(permission,
                    toolNames == null ? Collections.emptyList() : toolNames,
                    knowledgeReferences == null ? Collections.emptyList() : knowledgeReferences));
        } catch (Exception exception) {
            throw new AiProviderException(500, "保存 AI 消息元数据失败", exception);
        }
    }

    /**
     * 安全解析消息元数据；旧消息或无元数据时返回空对象。
     *
     * @param metadataJson JSON 元数据
     * @return 元数据对象
     */
    private AiMessageMetadata readMetadata(String metadataJson) {
        if (metadataJson == null || metadataJson.isBlank()) {
            return new AiMessageMetadata(null, Collections.emptyList(), Collections.emptyList());
        }
        try {
            AiMessageMetadata metadata = objectMapper.readValue(metadataJson, AiMessageMetadata.class);
            return new AiMessageMetadata(metadata.permission(), metadata.toolNames() == null
                    ? Collections.emptyList() : metadata.toolNames(), metadata.knowledgeReferences() == null
                    ? Collections.emptyList() : metadata.knowledgeReferences());
        } catch (Exception exception) {
            return new AiMessageMetadata(null, Collections.emptyList(), Collections.emptyList());
        }
    }

    /**
     * 将可空 Token 数值转换为零。
     *
     * @param value Token 数值
     * @return 非空数值
     */
    private long valueOrZero(Long value) {
        return value == null ? 0 : value;
    }
}
