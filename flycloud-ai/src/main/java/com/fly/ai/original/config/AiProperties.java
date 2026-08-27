package com.fly.ai.original.config;

import com.fly.common.enums.ai.AiProviderEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * AI 服务配置。配置统一放在 Nacos 的 {@code application-common.yaml} 中。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "flycloud.ai")
public class AiProperties {

    private boolean enabled = true;

    private AiProviderEnum provider = AiProviderEnum.DASHSCOPE;

    private String systemPrompt = "你是飞翔云系统的智能助手，请使用简洁、准确的中文回答。";

    private Integer maxOutputTokens = 1024;

    /** Tool Calling 相关配置。 */
    private ToolCalling toolCalling = new ToolCalling();

    private OpenAi openai = new OpenAi();

    private Deepseek deepseek = new Deepseek();

    private Dashscope dashscope = new Dashscope();

    @Getter
    @Setter
    public static class OpenAi {

        private String baseUrl = "https://api.openai.com";

        private String apiKey = "";

        private String chatModel = "gpt-5.4-mini";

        private String embeddingModel = "text-embedding-3-small";

        private String chatPath = "/v1/responses";

        private String embeddingPath = "/v1/embeddings";

        private Duration connectTimeout = Duration.ofSeconds(10);

        private Duration responseTimeout = Duration.ofSeconds(60);

    }

    /**
     * DeepSeek 配置。
     */
    @Getter
    @Setter
    public static class Deepseek {

        private String baseUrl = "https://api.deepseek.com";

        private String apiKey = "";

        private String chatModel = "deepseek-v4-flash";

        private String chatPath = "/chat/completions";

        private Duration responseTimeout = Duration.ofSeconds(60);
    }

    /**
     * 阿里云百炼（通义千问）配置。
     */
    @Getter
    @Setter
    public static class Dashscope {

        private String baseUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1";

        private String apiKey = "";

        private String chatModel = "qwen-plus";

        private String embeddingModel = "text-embedding-v4";

        private String chatPath = "/chat/completions";

        private String embeddingPath = "/embeddings";

        private Duration responseTimeout = Duration.ofSeconds(60);

    }

    /**
     * Tool Calling 安全配置。
     */
    @Getter
    @Setter
    public static class ToolCalling {

        /** 是否启用业务工具调用。 */
        private boolean enabled = true;
    }

}
