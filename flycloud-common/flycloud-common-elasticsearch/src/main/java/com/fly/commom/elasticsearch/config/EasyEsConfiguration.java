package com.fly.commom.elasticsearch.config;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * easy-es 配置
 *
 * @author lxs
 * @date 2023/5/20
 */
@Configuration
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
@EsMapperScan("com.fly.**.esmapper")
public class EasyEsConfiguration {
}
