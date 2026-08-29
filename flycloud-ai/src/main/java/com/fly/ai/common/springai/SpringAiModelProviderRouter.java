package com.fly.ai.common.springai;

import com.fly.common.enums.ai.AiProviderEnum;
import com.fly.ai.common.config.AiProperties;
import com.fly.common.utils.ai.AiUtils;
import com.fly.common.exception.AiProviderException;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.deepseek.autoconfigure.DeepSeekConnectionProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * Spring AI 模型供应商路由。
 * <p>
 * 与原生实现共同读取 {@code flycloud.ai.provider}，因此同一环境下两种实现始终调用相同供应商。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Component
public class SpringAiModelProviderRouter {

    private final AiProperties aiProperties;

    private final ChatClient openAiProtocolChatClient;

    private final ChatClient deepseekChatClient;

    private final OpenAiConnectionProperties openAiConnectionProperties;

    private final DeepSeekConnectionProperties deepseekConnectionProperties;

    private SelectedChatClient selectedChatClient;

    /**
     * 创建 Spring AI 模型供应商路由。
     *
     * @param aiProperties AI 公共配置
     * @param openAiProtocolChatClient OpenAI 协议聊天客户端
     * @param deepseekChatClient DeepSeek 聊天客户端
     * @param openAiConnectionProperties OpenAI 协议连接配置
     * @param deepseekConnectionProperties DeepSeek 连接配置
     */
    public SpringAiModelProviderRouter(AiProperties aiProperties,
            @Qualifier("openAiProtocolSpringAiChatClient") ChatClient openAiProtocolChatClient,
            @Qualifier("deepseekSpringAiChatClient") ChatClient deepseekChatClient,
            OpenAiConnectionProperties openAiConnectionProperties,
            DeepSeekConnectionProperties deepseekConnectionProperties) {
        this.aiProperties = aiProperties;
        this.openAiProtocolChatClient = openAiProtocolChatClient;
        this.deepseekChatClient = deepseekChatClient;
        this.openAiConnectionProperties = openAiConnectionProperties;
        this.deepseekConnectionProperties = deepseekConnectionProperties;
    }

    /**
     * 在 Bean 初始化阶段构建供应商客户端映射并缓存当前供应商客户端。
     * <p>
     * 模型客户端的地址、密钥与默认模型均在启动时创建，因此切换 {@code flycloud.ai.provider} 后必须重启服务。
     */
    @PostConstruct
    public void initializeSelectedChatClient() {
        Map<AiProviderEnum, ProviderClient> clients = new EnumMap<>(AiProviderEnum.class);
        clients.put(AiProviderEnum.DASHSCOPE, new ProviderClient(openAiProtocolChatClient,
                openAiConnectionProperties.getBaseUrl(), openAiConnectionProperties.getApiKey()));
        clients.put(AiProviderEnum.DEEPSEEK, new ProviderClient(deepseekChatClient,
                deepseekConnectionProperties.getBaseUrl(), deepseekConnectionProperties.getApiKey()));
        clients.put(AiProviderEnum.OPENAI, new ProviderClient(openAiProtocolChatClient,
                openAiConnectionProperties.getBaseUrl(), openAiConnectionProperties.getApiKey()));
        ProviderClient providerClient = clients.get(aiProperties.getProvider());
        if (providerClient == null) {
            throw new AiProviderException(503, "未找到 " + aiProperties.getProvider() + " 对应的 Spring AI 聊天客户端");
        }
        this.selectedChatClient = selectedChatClient(aiProperties.getProvider(), providerClient.chatClient(),
                providerClient.baseUrl(), providerClient.apiKey());
    }

    /**
     * 获取启动时已选择的 Spring AI 聊天客户端。
     *
     * @return 当前供应商的聊天客户端及展示名称
     */
    public SelectedChatClient getSelectedChatClient() {
        if (selectedChatClient == null) {
            throw new AiProviderException(503, "Spring AI 聊天客户端尚未初始化");
        }
        return selectedChatClient;
    }

    /**
     * 校验当前供应商的连接信息并返回其聊天客户端。
     *
     * @param provider 供应商枚举
     * @param chatClient Spring AI 聊天客户端
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @return 已校验的供应商聊天客户端信息
     */
    private SelectedChatClient selectedChatClient(AiProviderEnum provider, ChatClient chatClient,
                                                  String baseUrl, String apiKey) {
        String providerName = provider.getDisplayName() + "（Spring AI）";
        if (!AiUtils.hasText(apiKey)) {
            throw new AiProviderException(503, "未配置 " + providerName + " API Key，请检查 Nacos 配置或运行环境变量");
        }
        if (!AiUtils.hasText(baseUrl)) {
            throw new AiProviderException(503, providerName + " 的服务地址未配置");
        }
        return new SelectedChatClient(provider, providerName, chatClient);
    }

    /**
     * 当前供应商对应的聊天客户端信息。
     *
     * @param provider 供应商枚举
     * @param providerName 日志展示名称
     * @param chatClient Spring AI 聊天客户端
     */
    public record SelectedChatClient(AiProviderEnum provider, String providerName, ChatClient chatClient) {
    }

    /**
     * 供应商客户端初始化参数。
     *
     * @param chatClient Spring AI 聊天客户端
     * @param baseUrl 供应商服务地址
     * @param apiKey 供应商 API Key
     */
    private record ProviderClient(ChatClient chatClient, String baseUrl, String apiKey) {
    }
}
