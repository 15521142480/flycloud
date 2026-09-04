package com.fly.ai.common.config;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link AiRuntimeContextService} 单元测试。
 */
class AiRuntimeContextServiceTest {

    @Test
    void shouldUseServerClockAndConfiguredBusinessTimeZone() {
        AiProperties properties = new AiProperties();
        properties.setTimeZone("Asia/Shanghai");
        AiRuntimeContextService service = new AiRuntimeContextService(properties,
                Clock.fixed(Instant.parse("2026-09-04T08:30:00Z"), ZoneOffset.UTC));

        String context = service.trustedTimeContext();

        assertTrue(context.contains("2026-09-04 16:30:00"));
        assertTrue(context.contains("Asia/Shanghai"));
    }
}
