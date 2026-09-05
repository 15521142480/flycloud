package com.fly.common.database.handler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.Field;

/**
 * List&lt;Long&gt; JSON 类型处理器，兼容历史数据中的单个数值格式。
 *
 * <p>正常数据按 JSON 数组读写；历史标量数据会在读取时转换为单元素列表。</p>
 *
 * @author lxs
 */
@MappedTypes(Object.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JacksonLongListTypeHandler extends JacksonTypeHandler {

    /**
     * 使用字段类型创建处理器。
     *
     * @param type 字段类型
     */
    public JacksonLongListTypeHandler(Class<?> type) {
        super(type);
    }

    /**
     * 使用字段及其泛型信息创建处理器。
     *
     * @param type  字段类型
     * @param field 字段
     */
    public JacksonLongListTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    /**
     * 将 JSON 数组或单个数值转换为长整型列表。
     *
     * @param json 数据库中的 JSON 文本
     * @return 长整型列表
     */
    @Override
    public Object parse(String json) {
        ObjectMapper objectMapper = getObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(getFieldType());
        try {
            return objectMapper.readerFor(javaType)
                    .with(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                    .readValue(json);
        } catch (JacksonException e) {
            log.error("deserialize json: " + json + " to " + javaType + " error ", e);
            throw new RuntimeException(e);
        }
    }
}
