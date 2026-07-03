package com.fly.common.config;

import com.fly.common.config.properties.FileConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;

/**
 * 文件静态资源配置
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@AutoConfiguration
@EnableConfigurationProperties(FileConfigProperties.class)
@RequiredArgsConstructor
public class FileStaticResourceConfig implements WebFluxConfigurer {


    private final FileConfigProperties fileConfigProperties;

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String basePath = fileConfigProperties.getLocalServerConfig().getBasePath();
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }

        // 静态文件展示实现
        // 方案1: 要求：服务和文件都需要在同一个服务
        // Spring Security 放行 /static/**
        // 静态资源映射 /static/** -> basePath
        // 比如后端拼给前端的url：http://localhost:8080/static/mall/abc.png  会映射成： /Users/lxs/applicationFile/file/person/flycloud-server/mall/abc.png

        // 方案2: 场景：服务在本地启动，但是文件上传和展示都用服务器的
        // 这个类（FileStaticResourceConfig）都不需要配置了，因为不走网关了，直接走nginx
        // 在服务器上的nginx配置（注意base-url端口也要改，如：http://111.228.51.252:8888/static/）
        // server {
        //    listen 8888;
        //    server_name 111.228.51.252; # 推荐写localhost，写ip只是想表达是在服务器上而非本地的nginx
        //
        //    location /static/ {
        //        alias /project/upload-file/; # 也就是base-path
        //    }
        //}
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + basePath)
        ;
    }

}
