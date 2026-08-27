package com.fly.ai.toolcalling.model;

import com.fly.ai.common.model.AiPermission;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 单次 Tool Calling 请求的授权轨迹。
 * <p>
 * 该对象仅通过 Spring AI 的 {@code ToolContext} 在服务端传递，用户输入和模型参数均不能构造或修改它。
 *
 * @author lxs
 * @date 2026-08-27
 */
public final class AiToolAuthorizationTrace {

    private static final String PERMISSION_DENIED_MESSAGE = "检查到您没有该模块权限";

    private final Set<String> grantedMessages = new LinkedHashSet<>();

    private final Set<String> toolNames = new LinkedHashSet<>();

    private boolean denied;

    /**
     * 记录公共资源工具调用。
     *
     * @param toolName 工具名称
     */
    public synchronized void grantPublic(String toolName) {
        recordToolCall(toolName);
        grantedMessages.add("检查到您有该模块权限（公共模块）");
    }

    /**
     * 记录超级管理员授权结果。
     *
     * @param toolName 工具名称
     */
    public synchronized void grantSuperAdmin(String toolName) {
        recordToolCall(toolName);
        grantedMessages.add("检查到您有该模块权限（超级管理员）");
    }

    /**
     * 记录资源所属用户授权结果。
     *
     * @param toolName 工具名称
     */
    public synchronized void grantResourceOwner(String toolName) {
        recordToolCall(toolName);
        grantedMessages.add("检查到您有该模块权限（当前用户为订单用户）");
    }

    /**
     * 记录拒绝访问结果。
     *
     * @param toolName 工具名称
     */
    public synchronized void deny(String toolName) {
        recordToolCall(toolName);
        denied = true;
    }

    /**
     * 是否发生过工具调用。
     *
     * @return 发生过工具调用时返回 {@code true}
     */
    public synchronized boolean hasToolCall() {
        return !toolNames.isEmpty();
    }

    /**
     * 是否存在被拒绝的资源访问。
     *
     * @return 存在拒绝访问时返回 {@code true}
     */
    public synchronized boolean isDenied() {
        return denied;
    }

    /**
     * 返回最终响应应展示的权限说明文本。
     *
     * @return 权限提示；未调用工具时返回 {@code null}
     */
    public synchronized String permissionMessage() {
        AiPermission permission = permission();
        return permission == null ? null : permission.message();
    }

    /**
     * 返回结构化的最终资源权限结果。
     *
     * @return 权限结果；未调用工具时返回 {@code null}
     */
    public synchronized AiPermission permission() {
        if (!hasToolCall()) {
            return null;
        }
        if (denied) {
            return new AiPermission(false, PERMISSION_DENIED_MESSAGE);
        }
        return new AiPermission(true, String.join("\n", grantedMessages));
    }

    /**
     * 返回本次模型实际调用过的工具名称。
     *
     * @return 工具名称列表
     */
    public synchronized List<String> toolNames() {
        return List.copyOf(toolNames);
    }

    /**
     * 记录工具调用名称。
     *
     * @param toolName 工具名称
     */
    private void recordToolCall(String toolName) {
        toolNames.add(toolName);
    }
}
