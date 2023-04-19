package com.fly.gateway.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SpringDoc 配置
 *
 * @author lxs
 * @date 2023/4/19
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SpringDocConfiguration {


    private final RouteDefinitionLocator locator;


    /**
     * todo 分组文档OpenAPI (手动)
     *
     * 启动每个微服务都是一个个独立文档, swagger称之为一个个组名, 即 组名=服务名
     * 通过把当前服务的组名放到 swaggerUiConfigParameters.addGroup 中, 即添加到对应swagger-ui页面 definition 的下拉选项)
     *
     * 一旦您启动每个微服务，它将公开端点/v3/api-docs, 由上诉swagger设置好组名后, 其 OpenAPI 的url是 资源/v3/api-docs/{SERVICE_NAME} ，例如 http://localhost:8080/v3/api-docs/flycloud-system ,
     * 但这样的链接无法被网关识别, 本方案是在网关处理, 先将请求地址重写: /v3/api-docs/{SERVICE_NAME} 重写为 /{SERVICE_NAME}/v3/api-docs, 把重写后的地址代理到目标路由:  网关端口 + /{SERVICE_NAME}/v3/api-docs ,
     * 如 得到代理后的地址: http://localhost:8080/flycloud-system/v3/api-docs
     *
     *
     */
    @Bean
    @Lazy(false)
    @ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerDocProperties swaggerProperties) {

        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();

        // 将每个服务作为一个分组，比如edevp-org ，对应的value是org
        for (String value : swaggerProperties.getServices().values()) {
            swaggerUiConfigParameters.addGroup(value);
        }
        return groups;
    }


    /**
     * 分组文档OpenAPI (自动; 即 读取gateway的路由配置，截取服务前缀)
     *
     */
//    @Bean
//    public List<GroupedOpenApi> apis() {
//        List<GroupedOpenApi> groups = new ArrayList<>();
//        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//            String name = routeDefinition.getId().replaceAll("-service", "");
//            GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").setGroup(name).build();
//        });
//        return groups;
//    }



    @Data
    @Component
    @ConfigurationProperties("swagger")
    public class SwaggerDocProperties {

        private Map<String, String> services;

        /**
         * 认证参数
         */
        private SwaggerBasic basic = new SwaggerBasic();

        @Data
        public class SwaggerBasic {

            /**
             * 是否开启 basic 认证
             */
            private Boolean enabled;

            /**
             * 用户名
             */
            private String username;

            /**
             * 密码
             */
            private String password;

        }

    }

}
