package com.fly.ai.common.springai;

import com.fly.ai.common.config.AiProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.model.deepseek.autoconfigure.DeepSeekChatProperties;
import org.springframework.ai.model.deepseek.autoconfigure.DeepSeekConnectionProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Spring AI 客户端配置。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ OpenAiConnectionProperties.class, OpenAiChatProperties.class,
        DeepSeekConnectionProperties.class, DeepSeekChatProperties.class })
public class SpringAiConfiguration {

    /**
     * 创建基于 OpenAI 协议的 Spring AI 聊天客户端。
     * <p>
     * 当前示例中的 {@code spring.ai.openai.*} 指向阿里云百炼；该协议客户端也可在切换配置后调用 ChatGPT。
     *
     * @param connectionProperties OpenAI 协议连接配置
     * @param chatProperties OpenAI 协议聊天配置
     * @param aiProperties AI 公共配置
     * @return 带默认系统提示词的 OpenAI 协议聊天客户端
     */
    @Bean(name = "openAiProtocolSpringAiChatClient")
    public ChatClient openAiProtocolSpringAiChatClient(OpenAiConnectionProperties connectionProperties,
            OpenAiChatProperties chatProperties, AiProperties aiProperties) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(connectionProperties.getBaseUrl())
                .apiKey(apiKeyOrEmpty(connectionProperties.getApiKey()))
                .completionsPath(chatProperties.getCompletionsPath())
                .build();
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(chatProperties.getOptions())
                .build();
        return ChatClient.builder(chatModel).defaultSystem(aiProperties.getSystemPrompt()).build();
    }

    /**
     * 创建 DeepSeek 官方 Spring AI 聊天客户端。
     *
     * @param connectionProperties DeepSeek 连接配置
     * @param chatProperties DeepSeek 聊天配置
     * @param aiProperties AI 公共配置
     * @return 带默认系统提示词的 DeepSeek 聊天客户端
     */
    @Bean(name = "deepseekSpringAiChatClient")
    public ChatClient deepseekSpringAiChatClient(DeepSeekConnectionProperties connectionProperties,
            DeepSeekChatProperties chatProperties, AiProperties aiProperties) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
                .baseUrl(connectionProperties.getBaseUrl())
                .apiKey(apiKeyOrEmpty(connectionProperties.getApiKey()))
                .completionsPath(chatProperties.getCompletionsPath())
                .betaPrefixPath(chatProperties.getBetaPrefixPath())
                .build();
        DeepSeekChatModel chatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(chatProperties.getOptions())
                .build();
        return ChatClient.builder(chatModel).defaultSystem(aiProperties.getSystemPrompt()).build();
    }

    /**
     * 为未选中的供应商提供空 API Key 占位。
     * <p>
     * 多供应商客户端会在容器启动时一并创建；实际供应商的 API Key 由路由器在选择后校验，
     * 因此不应因未选中供应商尚未配置密钥而阻止服务启动。
     *
     * @param apiKey 原始 API Key
     * @return 非空 API Key 值
     */
    private String apiKeyOrEmpty(String apiKey) {
        return StringUtils.hasText(apiKey) ? apiKey : "";
    }
}
