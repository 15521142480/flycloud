package com.fly.common.doc.config;

import com.fly.common.doc.config.properties.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 *
 * <p>
 * 禁用方法1：使用注解@Profile({"dev","test"})
 * <p>
 * 表示在开发或测试环境开启，而在生产关闭。（推荐使用） 禁用方法2：使用注解@ConditionalOnProperty(name = "swagger.enable",
 * <p>
 * havingValue = "true") 然后在测试配置或者开发配置中添加swagger.enable=true即可开启，生产环境不填则默认关闭Swagger.
 * </p>
 *
 * @author lxs
 * @date 2023/4/19
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class SwaggerAutoConfiguration {

    private final SwaggerProperties swaggerProperties;

    private final ServiceInstance serviceInstance;

    @Bean
    public OpenAPI springOpenAPI() {

//        OpenAPI openAPI = new OpenAPI().info(new Info().title(swaggerProperties.getTitle()));
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(new Info().title(swaggerProperties.getTitle()));

        // oauth2.0 password
        openAPI.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
        openAPI.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme());

        // servers 提供调用的接口地址前缀
        System.out.println("swaggerProperties.getServices():" + swaggerProperties.getServices().toString());
        System.out.println("serviceInstance.getServiceId():" + serviceInstance.getServiceId());
        List<Server> serverList = new ArrayList<>();
        String path = swaggerProperties.getServices().get(serviceInstance.getServiceId());
        serverList.add(new Server().url(swaggerProperties.getGateway() + "/" + path));
        openAPI.servers(serverList);

        return openAPI;
    }

    // 全局添加token
    private SecurityScheme securityScheme() {

        SecurityScheme securityScheme = new SecurityScheme();
        //类型
        securityScheme.setType(SecurityScheme.Type.APIKEY);
        //请求头的name
        securityScheme.setName(HttpHeaders.AUTHORIZATION);
        //token所在未知
        securityScheme.setIn(SecurityScheme.In.HEADER);
        return securityScheme;
    }
}
