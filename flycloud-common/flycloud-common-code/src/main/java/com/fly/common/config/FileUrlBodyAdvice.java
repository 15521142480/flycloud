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

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        convertRequestValue(body, new IdentityHashMap<>());
        return body;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

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

    private Collection<Field> getAllFields(Class<?> type) {
        java.util.List<Field> fields = new java.util.ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            fields.addAll(java.util.Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    private boolean shouldSkipField(Field field) {
        int modifiers = field.getModifiers();
        return java.lang.reflect.Modifier.isStatic(modifiers);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replaceMapValues(Map<?, ?> map, java.util.function.Function<Object, Object> valueConverter) {
        ((Map) map).replaceAll((key, value) -> valueConverter.apply(value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replaceMapValues(Map<?, ?> map, java.util.function.BiFunction<Object, Object, Object> valueConverter) {
        ((Map) map).replaceAll((key, value) -> valueConverter.apply(key, value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replaceListValue(java.util.ListIterator<?> iterator, Object value) {
        ((java.util.ListIterator) iterator).set(value);
    }

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

    private String toPath(String urlOrPath) {
        return fileUrlConverter.toPath(urlOrPath);
    }

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
