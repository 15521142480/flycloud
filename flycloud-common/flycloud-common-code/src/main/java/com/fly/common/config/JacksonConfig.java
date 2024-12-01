package com.fly.common.config;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fly.common.utils.json.JsonUtils;
import com.fly.common.utils.json.databind.NumberSerializer;
import com.fly.common.utils.json.databind.TimestampLocalDateTimeDeserializer;
import com.fly.common.utils.json.databind.TimestampLocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * jackson 配置
 *
 * @author lxs
 */
@Slf4j
@Configuration
public class JacksonConfig {


    @Bean
    public JsonUtils jsonUtils(List<ObjectMapper> objectMappers) {

        // 1.1 创建 SimpleModule 对象
        SimpleModule simpleModule = new SimpleModule();
        simpleModule
                // 新增 Long 类型序列化规则，数值超过 2^53-1，在 JS 会出现精度丢失问题，因此 Long 自动序列化为字符串类型
                .addSerializer(Long.class, NumberSerializer.INSTANCE)
                .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                .addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE)
                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE)
                .addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                // 新增 LocalDateTime 序列化、反序列化规则，使用 Long 时间戳
                .addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
                .addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
        // 1.2 注册到 objectMapper
        objectMappers.forEach(objectMapper -> objectMapper.registerModule(simpleModule));

        // 2. 设置 objectMapper 到 JsonUtils
        JsonUtils.init(CollUtil.getFirst(objectMappers));
        log.info("[init][初始化 JsonUtils 成功]");

        return new JsonUtils();
    }

    /**
     * 全局配置序列化返回 JSON 处理
     */
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer customizer() {
//
//        return builder -> {
//
//            JavaTimeModule javaTimeModule = new JavaTimeModule();
//            javaTimeModule.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
//            javaTimeModule.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
//            javaTimeModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
//            javaTimeModule.addSerializer(BigDecimal.class, ToNumberSerializer.INSTANCE);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
//            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//            builder.modules(javaTimeModule);
//            builder.timeZone(TimeZone.getDefault());
//            log.info("初始化 jackson 配置");
//        };
//    }

}
