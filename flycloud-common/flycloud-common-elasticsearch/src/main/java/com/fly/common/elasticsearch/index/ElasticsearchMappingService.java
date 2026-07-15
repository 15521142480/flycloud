package com.fly.common.elasticsearch.index;

import com.fly.common.elasticsearch.exception.ElasticsearchIndexException;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/** 从 classpath 加载经版本控制的业务 Mapping JSON。 */
public class ElasticsearchMappingService {

    /** 加载业务索引定义的 JSON Mapping。 */
    public Reader load(String resourcePath) {
        try {
            return new InputStreamReader(new ClassPathResource(resourcePath).getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("加载 Mapping 失败：" + resourcePath);
        }
    }
}
