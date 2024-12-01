package com.fly.common.utils.json.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimal序列化成Number对象
 *
 */
@JacksonStdImpl
public class ToNumberSerializer extends StdScalarSerializer<BigDecimal> {

    /**
     * 提供实例
     */
    public static final ToNumberSerializer INSTANCE = new ToNumberSerializer(BigDecimal.class);

    public ToNumberSerializer(Class<? extends BigDecimal> rawType) {
        super(rawType, false);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value);
    }
}
