package com.fly.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 网关跨域配置
 *
 * @author lxs
 * @date 2026-06-29
 */
@Configuration
public class GatewayCorsConfiguration {

    private static final long MAX_AGE = 24 * 60 * 60L;

    private static final int CORS_RESPONSE_HEADER_FILTER_ORDER = Ordered.LOWEST_PRECEDENCE;

    /**
     * 处理浏览器到网关的跨域预检请求。
     */
    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(false);
        config.setMaxAge(MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

    /**
     * 去除下游服务和网关重复写入的跨域响应头。
     */
    @Bean
    public GlobalFilter corsResponseHeaderFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().beforeCommit(() -> {
                HttpHeaders headers = exchange.getResponse().getHeaders();
                String origin = exchange.getRequest().getHeaders().getOrigin();
                dedupeHeader(headers, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                dedupeHeader(headers, HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, null);
                return Mono.empty();
            });
            return chain.filter(exchange);
        };
    }

    /**
     * 保留唯一的跨域响应头，优先使用本次请求的 Origin。
     */
    private void dedupeHeader(HttpHeaders headers, String headerName, String preferredValue) {
        List<String> values = headers.get(headerName);
        if (values == null || values.size() <= 1) {
            return;
        }
        if (preferredValue != null && !preferredValue.isBlank()) {
            headers.set(headerName, preferredValue);
            return;
        }
        headers.set(headerName, values.get(0));
    }

}
