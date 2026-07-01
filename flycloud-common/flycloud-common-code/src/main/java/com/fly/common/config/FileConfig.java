package com.fly.common.config;

import com.fly.common.config.properties.FileConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/**
 * 文件配置
 *
 * @author: lxs
 * @date: 2026/7/1
 */
@AutoConfiguration
@EnableConfigurationProperties(FileConfigProperties.class)
public class FileConfig {


}
