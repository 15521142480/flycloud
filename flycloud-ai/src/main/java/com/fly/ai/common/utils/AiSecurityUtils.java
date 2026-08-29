package com.fly.ai.common.utils;

import com.fly.common.exception.AiProviderException;
import com.fly.common.security.util.UserUtils;

/**
 * AI 模块的认证用户工具。
 * <p>
 * 所有 AI Controller 均只从服务端安全上下文读取登录用户，禁止接收前端传入的 userId。
 *
 * @author lxs
 * @date 2026-08-28
 */
public final class AiSecurityUtils {

    private AiSecurityUtils() {
    }

    /**
     * 获取当前已认证用户编号。
     *
     * @param capability 当前使用的 AI 能力名称，用于返回明确的认证提示
     * @return 当前登录用户编号
     */
    public static Long requiredLoginUserId(String capability) {
        Long loginUserId = UserUtils.getCurUserId();
        if (loginUserId == null) {
            throw new AiProviderException(401, "请先登录后再使用 " + capability);
        }
        return loginUserId;
    }
}
