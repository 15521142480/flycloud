package com.fly.ai.common.controller;

import com.fly.ai.common.model.AiChatRuntimeConfiguration;
import com.fly.ai.original.config.AiProperties;
import com.fly.common.domain.model.R;
import com.fly.common.enums.ai.AiProviderEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * AI 聊天运行配置接口。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Tag(name = "AI 聊天运行配置")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class AiChatRuntimeConfigurationController {

    private final AiProperties aiProperties;

    /**
     * 获取当前 AI 聊天页面可安全展示的运行配置。
     *
     * @return 当前供应商、默认模型及已集成供应商列表
     */
    @Operation(summary = "获取 AI 聊天运行配置", description = "仅返回供应商和默认模型，不会返回 API Key 或服务地址")
    @GetMapping("/runtime-configuration")
    public R<AiChatRuntimeConfiguration> getRuntimeConfiguration() {
        AiProviderEnum provider = aiProperties.getProvider();
        return R.ok(new AiChatRuntimeConfiguration(provider.getValue(), provider.getDisplayName(),
                configuredChatModel(provider), Arrays.stream(AiProviderEnum.values())
                        .map(AiChatRuntimeConfiguration.ProviderOption::from)
                        .toList()));
    }

    /**
     * 根据当前供应商读取其默认聊天模型。
     *
     * @param provider 当前供应商
     * @return 默认聊天模型
     */
    private String configuredChatModel(AiProviderEnum provider) {
        return switch (provider) {
            case OPENAI -> aiProperties.getOpenai().getChatModel();
            case DEEPSEEK -> aiProperties.getDeepseek().getChatModel();
            case DASHSCOPE -> aiProperties.getDashscope().getChatModel();
        };
    }
}
