package com.fly.ai.common.model;

import com.fly.common.enums.ai.AiProviderEnum;

import java.util.List;

/**
 * 面向聊天页面的 AI 运行配置。
 * <p>
 * 仅暴露可安全展示的供应商和模型信息，绝不返回 API Key、服务地址及其他连接配置。
 *
 * @param provider 当前生效的供应商配置值
 * @param providerName 当前生效的供应商名称
 * @param chatModel 当前供应商的默认聊天模型
 * @param providers 系统已集成的供应商列表
 */
public record AiChatRuntimeConfiguration(String provider, String providerName, String chatModel,
        List<ProviderOption> providers) {

    /**
     * 供应商下拉项。
     *
     * @param value Nacos 配置值
     * @param label 页面展示名称
     */
    public record ProviderOption(String value, String label) {

        /**
         * 由供应商枚举创建安全展示项。
         *
         * @param provider 模型供应商
         * @return 供应商下拉项
         */
        public static ProviderOption from(AiProviderEnum provider) {
            return new ProviderOption(provider.getValue(), provider.getDisplayName());
        }
    }
}
