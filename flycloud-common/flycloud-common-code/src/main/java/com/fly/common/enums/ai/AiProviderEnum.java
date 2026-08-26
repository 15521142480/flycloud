package com.fly.common.enums.ai;

/**
 * AI 模型供应商。
 * <p>
 * 枚举名称可直接作为 Nacos {@code flycloud.ai.provider} 配置值，Spring Boot 可忽略大小写绑定。
 * 所有供应商路由必须使用本枚举，禁止散落书写供应商字符串。
 *
 * @author lxs
 * @date 2026-08-26
 */
public enum AiProviderEnum {

    /** OpenAI 官方服务。 */
    OPENAI("OpenAI"),

    /** DeepSeek 官方服务。 */
    DEEPSEEK("DeepSeek"),

    /** 阿里云百炼（通义千问）服务。 */
    DASHSCOPE("阿里云百炼");

    private final String displayName;

    AiProviderEnum(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取 Nacos 配置值。
     *
     * @return 小写供应商配置值
     */
    public String getValue() {
        return name().toLowerCase();
    }

    /**
     * 获取用于日志和错误消息的供应商名称。
     *
     * @return 供应商展示名称
     */
    public String getDisplayName() {
        return displayName;
    }

}
