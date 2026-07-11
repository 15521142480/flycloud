package com.fly.common.utils.json.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * 数字型标识符 JSON 序列化器。
 * <p>
 * 标识符只用于等值匹配，不参与数值运算，因此单值和集合元素均使用字符串输出。
 *
 * @author lxs
 * @date 2026-07-11
 */
public class JsonLongIdSerializer extends StdSerializer<Object> {

    public JsonLongIdSerializer() {
        super(Object.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (value instanceof Iterable<?> iterable) {
            generator.writeStartArray();
            for (Object item : iterable) {
                writeId(item, generator, provider);
            }
            generator.writeEndArray();
            return;
        }
        if (value.getClass().isArray()) {
            generator.writeStartArray();
            for (int index = 0; index < Array.getLength(value); index++) {
                writeId(Array.get(value, index), generator, provider);
            }
            generator.writeEndArray();
            return;
        }
        writeId(value, generator, provider);
    }

    /**
     * 输出单个标识符，兼容后端 Long 和已完成字符串化的标识符。
     */
    private void writeId(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (value == null) {
            provider.defaultSerializeNull(generator);
            return;
        }
        if (value instanceof Number || value instanceof CharSequence) {
            generator.writeString(value.toString());
            return;
        }
        throw JsonMappingException.from(generator,
                "@JsonLongId 仅支持 Number、字符串、标识符集合或标识符数组，当前类型：" + value.getClass().getName());
    }

}
