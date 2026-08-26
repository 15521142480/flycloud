package com.fly.ai.springai.service;

import com.fly.common.enums.ai.AiProviderEnum;
import com.fly.ai.original.config.AiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.deepseek.autoconfigure.DeepSeekConnectionProperties;
import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

/**
 * 验证 Spring AI 与原生实现共用的供应商路由规则。
 */
class SpringAiModelProviderRouterTest {

    /**
     * provider 为 dashscope 时，应选择 OpenAI 协议的百炼客户端。
     */
    @Test
    void shouldRouteDashscopeToOpenAiProtocolClient() {
        AiProperties properties = properties(AiProviderEnum.DASHSCOPE);
        ChatClient openAiProtocolClient = mock(ChatClient.class);
        SpringAiModelProviderRouter router = new SpringAiModelProviderRouter(properties, openAiProtocolClient,
                mock(ChatClient.class), openAiProperties(), deepseekProperties());
        router.initializeSelectedChatClient();

        SpringAiModelProviderRouter.SelectedChatClient selected = router.getSelectedChatClient();

        assertEquals(AiProviderEnum.DASHSCOPE, selected.provider());
        assertEquals("阿里云百炼（Spring AI）", selected.providerName());
        assertSame(openAiProtocolClient, selected.chatClient());
    }

    /**
     * provider 为 deepseek 时，应选择 DeepSeek 官方客户端。
     */
    @Test
    void shouldRouteDeepseekToDeepseekClient() {
        AiProperties properties = properties(AiProviderEnum.DEEPSEEK);
        ChatClient deepseekClient = mock(ChatClient.class);
        SpringAiModelProviderRouter router = new SpringAiModelProviderRouter(properties, mock(ChatClient.class),
                deepseekClient, openAiProperties(), deepseekProperties());
        router.initializeSelectedChatClient();

        SpringAiModelProviderRouter.SelectedChatClient selected = router.getSelectedChatClient();

        assertEquals(AiProviderEnum.DEEPSEEK, selected.provider());
        assertEquals("DeepSeek（Spring AI）", selected.providerName());
        assertSame(deepseekClient, selected.chatClient());
    }

    /**
     * 仅校验启动时选中的供应商，未选中供应商不应因为未配置 API Key 阻止服务启动。
     */
    @Test
    void shouldNotRequireApiKeyForUnselectedProvider() {
        AiProperties properties = properties(AiProviderEnum.DASHSCOPE);
        DeepSeekConnectionProperties deepseekProperties = new DeepSeekConnectionProperties();
        SpringAiModelProviderRouter router = new SpringAiModelProviderRouter(properties, mock(ChatClient.class),
                mock(ChatClient.class), openAiProperties(), deepseekProperties);

        assertDoesNotThrow(router::initializeSelectedChatClient);
    }

    /**
     * 创建仅设置 provider 的测试配置。
     *
     * @param provider 供应商枚举
     * @return AI 配置
     */
    private AiProperties properties(AiProviderEnum provider) {
        AiProperties properties = new AiProperties();
        properties.setProvider(provider);
        return properties;
    }

    /**
     * 创建 OpenAI 协议连接测试配置。
     *
     * @return OpenAI 协议连接配置
     */
    private OpenAiConnectionProperties openAiProperties() {
        OpenAiConnectionProperties properties = new OpenAiConnectionProperties();
        properties.setApiKey("test-openai-key");
        return properties;
    }

    /**
     * 创建 DeepSeek 连接测试配置。
     *
     * @return DeepSeek 连接配置
     */
    private DeepSeekConnectionProperties deepseekProperties() {
        DeepSeekConnectionProperties properties = new DeepSeekConnectionProperties();
        properties.setApiKey("test-deepseek-key");
        return properties;
    }
}
