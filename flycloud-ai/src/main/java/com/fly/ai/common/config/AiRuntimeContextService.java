package com.fly.ai.common.config;

import com.fly.common.utils.ai.AiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 为模型请求生成可信的运行时上下文。
 * <p>
 * 当前时间等易变化事实必须由服务端提供，不能让模型依据训练数据猜测。外部实时数据（天气、行情、新闻等）
 * 仍必须由经过审批的业务 Tool 或数据源提供。
 *
 * @author lxs
 * @date 2026-09-04
 */
@Service
@RequiredArgsConstructor
public class AiRuntimeContextService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    private final AiProperties aiProperties;

    private final Clock aiClock;

    /**
     * 构建包含基础提示词、可信运行时上下文和业务策略的系统提示词。
     *
     * @param policy 当前能力的行为策略
     * @param supplementalContext 已验证的补充上下文，例如 RAG 检索结果
     * @return 每次请求动态生成的系统提示词
     */
    public String systemPrompt(String policy, String supplementalContext) {
        StringBuilder prompt = new StringBuilder(aiProperties.getSystemPrompt())
                .append("\n\n")
                .append(trustedTimeContext());
        if (AiUtils.hasText(policy)) {
            prompt.append("\n\n").append(policy);
        }
        if (AiUtils.hasText(supplementalContext)) {
            prompt.append("\n\n").append(supplementalContext);
        }
        return prompt.toString();
    }

    /**
     * 返回由服务端时钟生成的可信时间事实。
     *
     * @return 可注入模型系统提示词的时间上下文
     */
    public String trustedTimeContext() {
        ZonedDateTime now = ZonedDateTime.ofInstant(aiClock.instant(), ZoneId.of(aiProperties.getTimeZone()));
        return "【可信运行时上下文】\n当前服务端日期时间：" + DATE_TIME_FORMATTER.format(now)
                + "；业务时区：" + aiProperties.getTimeZone()
                + "。涉及当前、今天、明天、今年、相对日期或日期计算时，必须以此时间为准，不得依据模型训练知识猜测。";
    }
}
