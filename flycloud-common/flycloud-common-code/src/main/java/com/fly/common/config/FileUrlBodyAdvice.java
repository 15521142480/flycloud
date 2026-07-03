package com.fly.common.config;

import cn.hutool.core.util.StrUtil;
import com.fly.common.file.FileUrlConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 统一处理文件字段的存储值和展示值：
 * 入参：当前 base-url 下的完整 URL 转为 path；
 * 出参：图片、文件字段中的 path 转为完整 URL。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Component
@ControllerAdvice
@RequiredArgsConstructor
public class FileUrlBodyAdvice extends RequestBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private static final Set<String> FILE_FIELD_NAMES = Set.of(
            "avatar", "picUrl", "picUrls", "sliderPicUrls", "coverUrl", "logo",
            "backgroundUrl", "fileUrl", "fileUrls", "previewPicUrls", "brokeragePosterUrls",
            "userAvatar", "materialCoverUrl", "images", "imgUrl", "bgImg", "bgImgUrl",
            "backgroundImage", "imageUrl", "skuPicUrl", "applyPicUrls", "qrCodeUrl"
    );

    private static final Set<String> FILE_FIELD_TOKENS = Set.of(
            "avatar", "pic", "image", "img", "logo", "cover", "background", "file", "poster"
    );

    private final FileUrlConverter fileUrlConverter;

    /**
     * 启用请求体处理。
     *
     * @return true 表示所有 @RequestBody 入参都进入 afterBodyRead 统一转换
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 请求体读取完成后执行，将当前 base-url 下的完整 URL 还原为数据库需要存储的 path。
     *
     * @param body 反序列化后的请求对象
     * @return 转换后的请求对象
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        convertRequestValue(body, new IdentityHashMap<>());
        return body;
    }

    /**
     * 启用响应体处理。
     *
     * @return true 表示所有响应对象都进入 beforeBodyWrite 统一转换
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 响应写出前执行，将文件字段中的 path 拼接为前端可展示的完整 URL。
     *
     * @param body 响应对象
     * @param selectedContentType 响应类型，仅处理 JSON
     * @return 转换后的响应对象
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {
        if (selectedContentType != null && !MediaType.APPLICATION_JSON.includes(selectedContentType)) {
            return body;
        }
        convertResponseValue(null, body, new IdentityHashMap<>());
        return body;
    }

    /**
     * 递归处理请求对象，把字符串里的当前 base-url 前缀去掉，避免完整 URL 入库。
     *
     * @param value 当前待处理对象
     * @param visited 已访问对象，用于避免循环引用导致无限递归
     * @return 转换后的对象；集合和普通 bean 会原地修改
     */
    private Object convertRequestValue(Object value, IdentityHashMap<Object, Boolean> visited) {
        if (value == null || isSimpleValue(value)) {
            return value instanceof String text ? toPath(text) : value;
        }
        if (visited.containsKey(value)) {
            return value;
        }
        visited.put(value, Boolean.TRUE);
        if (value instanceof java.util.List<?> list) {
            java.util.ListIterator<?> iterator = list.listIterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                Object converted = convertRequestValue(item, visited);
                if (converted != item) {
                    replaceListValue(iterator, converted);
                }
            }
            return value;
        }
        if (value instanceof Collection<?> collection) {
            for (Object item : collection) {
                convertRequestValue(item, visited);
            }
            return value;
        }
        if (value instanceof Map<?, ?> map) {
            replaceMapValues(map, item -> convertRequestValue(item, visited));
            return value;
        }
        for (Field field : getAllFields(value.getClass())) {
            if (shouldSkipField(field)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                Object converted = convertRequestValue(fieldValue, visited);
                if (converted != fieldValue && !java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
                    field.set(value, converted);
                }
            } catch (IllegalAccessException ignored) {
                // 保持原值
            }
        }
        return value;
    }

    /**
     * 递归处理响应对象，只对文件字段把 path 拼接为完整 URL。
     *
     * @param fieldName 当前字段名，用于判断是否属于文件字段
     * @param value 当前待处理对象
     * @param visited 已访问对象，用于避免循环引用导致无限递归
     * @return 转换后的对象；集合和普通 bean 会原地修改
     */
    private Object convertResponseValue(String fieldName, Object value, IdentityHashMap<Object, Boolean> visited) {
        if (value == null) {
            return null;
        }
        if (value instanceof String text) {
            return isFileField(fieldName) ? fileUrlConverter.buildUrl(text) : text;
        }
        if (isSimpleValue(value)) {
            return value;
        }
        if (visited.containsKey(value)) {
            return value;
        }
        visited.put(value, Boolean.TRUE);
        if (value instanceof Collection<?> collection) {
            if (isFileField(fieldName)) {
                return collection.stream().map(item -> convertResponseValue(fieldName, item, visited)).toList();
            }
            collection.forEach(item -> convertResponseValue(null, item, visited));
            return value;
        }
        if (value instanceof Map<?, ?> map) {
            replaceMapValues(map, (key, item) -> convertResponseValue(String.valueOf(key), item, visited));
            return value;
        }
        for (Field field : getAllFields(value.getClass())) {
            if (shouldSkipField(field)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                Object converted = convertResponseValue(field.getName(), fieldValue, visited);
                if (converted != fieldValue && !java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
                    field.set(value, converted);
                }
            } catch (IllegalAccessException ignored) {
                // 保持原值
            }
        }
        return value;
    }

    /**
     * 获取当前类及其父类声明的所有字段。
     *
     * @param type 目标类型
     * @return 字段集合
     */
    private Collection<Field> getAllFields(Class<?> type) {
        java.util.List<Field> fields = new java.util.ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            fields.addAll(java.util.Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * 判断字段是否跳过转换。
     *
     * @param field 字段对象
     * @return true 表示不参与递归转换
     */
    private boolean shouldSkipField(Field field) {
        int modifiers = field.getModifiers();
        return java.lang.reflect.Modifier.isStatic(modifiers);
    }

    /**
     * 替换 Map 中的 value，适用于不需要 key 参与判断的场景。
     *
     * @param map 待处理 Map
     * @param valueConverter value 转换函数
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replaceMapValues(Map<?, ?> map, java.util.function.Function<Object, Object> valueConverter) {
        ((Map) map).replaceAll((key, value) -> valueConverter.apply(value));
    }

    /**
     * 替换 Map 中的 value，适用于需要 key 作为字段名判断的场景。
     *
     * @param map 待处理 Map
     * @param valueConverter key 和 value 转换函数
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replaceMapValues(Map<?, ?> map, java.util.function.BiFunction<Object, Object, Object> valueConverter) {
        ((Map) map).replaceAll((key, value) -> valueConverter.apply(key, value));
    }

    /**
     * 替换 ListIterator 当前元素。
     *
     * @param iterator 当前列表迭代器
     * @param value 新值
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replaceListValue(java.util.ListIterator<?> iterator, Object value) {
        ((java.util.ListIterator) iterator).set(value);
    }

    /**
     * 判断对象是否为简单值，简单值不继续展开字段递归。
     *
     * @param value 待判断对象
     * @return true 表示数字、布尔、日期、枚举、JDK 类型等简单值
     */
    private boolean isSimpleValue(Object value) {
        Class<?> type = value.getClass();
        return type.isPrimitive()
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Date
                || value instanceof Temporal
                || value instanceof Enum<?>
                || type.getName().startsWith("java.");
    }

    /**
     * 将当前 base-url 下的完整 URL 转为相对 path。
     *
     * @param urlOrPath 完整 URL 或相对 path
     * @return 相对 path；非当前 base-url 地址保持原值
     */
    private String toPath(String urlOrPath) {
        return fileUrlConverter.toPath(urlOrPath);
    }

    /**
     * 根据字段名判断是否应该按文件字段处理。
     *
     * @param fieldName 字段名
     * @return true 表示响应时需要把 path 拼接为完整 URL
     */
    private boolean isFileField(String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return false;
        }
        if (FILE_FIELD_NAMES.contains(fieldName)) {
            return true;
        }
        if (!StrUtil.endWithAny(fieldName, "Url", "Urls")) {
            return false;
        }
        String lowerName = fieldName.toLowerCase();
        return FILE_FIELD_TOKENS.stream().anyMatch(lowerName::contains);
    }

}
