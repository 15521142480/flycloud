package com.fly.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fly.common.utils.json.databind.JsonLongIdSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将数字型标识符统一序列化为 JSON 字符串。
 * <p>
 * 主要用于 Long 标识符，同时支持 Integer 等数字型标识符、标识符集合和标识符数组，
 * 避免前端因 JavaScript 数字精度或运行时类型不一致导致匹配失败。
 *
 * @author lxs
 * @date 2026-07-11
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonLongIdSerializer.class)
public @interface JsonLongId {
}
