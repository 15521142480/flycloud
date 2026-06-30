package com.fly.im.framework.web.config;

import org.springdoc.core.models.GroupedOpenApi;

/**
 * IM Swagger 分组适配。
 *
 * @author lxs
 * @date 2026-06-30
 */
public class ImSwaggerAutoConfiguration {

    public static GroupedOpenApi buildGroupedOpenApi(String group) {
        return GroupedOpenApi.builder().group(group).pathsToMatch("/" + group + "/**").build();
    }

}
