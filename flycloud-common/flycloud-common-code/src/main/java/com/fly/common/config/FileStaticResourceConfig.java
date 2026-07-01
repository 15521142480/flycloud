package com.fly.common.config;

import com.fly.common.config.properties.FileConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件静态资源配置
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@AutoConfiguration
@RequiredArgsConstructor
public class FileStaticResourceConfig implements WebMvcConfigurer {


    private final FileConfigProperties fileConfigProperties;

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String basePath = fileConfigProperties.getLocalServerConfig().getBasePath();
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }

        // 静态文件展示实现：
        // Spring Security 放行 /static/**
        // 静态资源映射 /static/** -> basePath
        // 比如后端拼给前端的url：http://localhost:8080/static/mall/abc.png  会映射成： /Users/lxs/applicationFile/file/person/flycloud-server/mall/abc.png
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + basePath)
        ;
    }

}
