package com.fly.gateway.config;

import com.fly.common.doc.config.properties.SwaggerProperties;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SpringDoc 配置
 *
 * @author lxs
 * @date 2026/4/19
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfiguration {

    /**
     * 注册网关聚合文档的服务分组。
     *
     * @param swaggerUiConfigProperties Swagger UI 配置属性
     * @param swaggerProperties         公共 Swagger 配置
     * @param applicationName           当前网关服务名
     * @return 空分组集合；远程服务文档由网关代理，不在网关本地扫描 Controller
     */
    @Bean
    @Lazy(false)
    @ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
    public List<GroupedOpenApi> apis(SwaggerUiConfigProperties swaggerUiConfigProperties,
                                     SwaggerProperties swaggerProperties,
                                     @Value("${spring.application.name}") String applicationName) {

        Map<String, String> services = swaggerProperties.getServices();
        if (services == null || services.isEmpty()) {
            return Collections.emptyList();
        }

        Set<SwaggerUrl> urls = swaggerUiConfigProperties.getUrls() == null
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(swaggerUiConfigProperties.getUrls());
        services.forEach((serviceName, routePrefix) -> {
            // /v3/api-docs/{group} 是 SpringDoc 在网关本地解析分组文档的端点，
            // 不能用于代理远程服务；否则会被本地处理器拦截并提示分组不存在。
            String apiDocsUrl = applicationName.equals(serviceName)
                    ? "/v3/api-docs"
                    : "/" + routePrefix + "/v3/api-docs";
            urls.add(new SwaggerUrl(serviceName, apiDocsUrl, null));
        });
        swaggerUiConfigProperties.setUrls(urls);
        return Collections.emptyList();
    }

}
