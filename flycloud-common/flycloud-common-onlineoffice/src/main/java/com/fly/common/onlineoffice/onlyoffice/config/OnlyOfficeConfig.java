package com.fly.common.onlineoffice.onlyoffice.config;

import com.fly.common.onlineoffice.onlyoffice.config.properties.OnlyOfficeProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * OnlyOffice配置
 *
 * @author: lxs
 * @date: 2026/7/12
 */
@AutoConfiguration
@Import({
        OnlyOfficeProperties.class
})
public class OnlyOfficeConfig {
}
