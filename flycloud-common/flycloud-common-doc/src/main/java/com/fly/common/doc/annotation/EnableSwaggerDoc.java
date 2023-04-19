package com.fly.common.doc.annotation;

import com.fly.common.doc.config.SwaggerAutoConfiguration;
import com.fly.common.doc.config.properties.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 spring doc (只有在Application中添加如下注解才能启用文档)
 *
 * @author lxs
 * @date 2023/4/19
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({ SwaggerAutoConfiguration.class })
public @interface EnableSwaggerDoc {

}
