package com.fly.common.file;

import com.fly.common.domain.vo.PageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * 按字段名显式转换对象里的文件字段。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Component
@RequiredArgsConstructor
public class FileUrlFieldConverter {

    private final FileUrlConverter fileUrlConverter;

    public <T> T buildUrl(T bean, String... fieldNames) {
        convert(bean, true, fieldNames);
        return bean;
    }

    public <T> List<T> buildUrlList(List<T> list, String... fieldNames) {
        convert(list, true, fieldNames);
        return list;
    }

    public <T> PageVo<T> buildUrlPage(PageVo<T> page, String... fieldNames) {
        if (page != null) {
            convert(page.getList(), true, fieldNames);
        }
        return page;
    }

    public <T> T toPath(T bean, String... fieldNames) {
        convert(bean, false, fieldNames);
        return bean;
    }

    public <T> List<T> toPathList(List<T> list, String... fieldNames) {
        convert(list, false, fieldNames);
        return list;
    }

    private void convert(Object target, boolean buildUrl, String... fieldNames) {
        if (target == null || fieldNames == null) {
            return;
        }
        if (target instanceof Collection<?> collection) {
            collection.forEach(item -> convert(item, buildUrl, fieldNames));
            return;
        }
        for (String fieldName : fieldNames) {
            convertField(target, fieldName, buildUrl);
        }
    }

    private void convertField(Object target, String fieldName, boolean buildUrl) {
        if (target == null || fieldName == null) {
            return;
        }
        int dotIndex = fieldName.indexOf('.');
        if (dotIndex > 0) {
            Object nested = getFieldValue(target, fieldName.substring(0, dotIndex));
            convert(nested, buildUrl, fieldName.substring(dotIndex + 1));
            return;
        }
        Field field = findField(target.getClass(), fieldName);
        if (field == null) {
            return;
        }
        try {
            field.setAccessible(true);
            Object value = field.get(target);
            Object converted = convertValue(value, buildUrl);
            if (converted != value && !java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
                field.set(target, converted);
            }
        } catch (IllegalAccessException ignored) {
            // 保持原值
        }
    }

    private Object getFieldValue(Object target, String fieldName) {
        Field field = findField(target.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException ignored) {
            return null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object convertValue(Object value, boolean buildUrl) {
        if (value instanceof String text) {
            return buildUrl ? fileUrlConverter.buildUrl(text) : fileUrlConverter.toPath(text);
        }
        if (value instanceof List<?> list) {
            ListIterator iterator = list.listIterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof String text) {
                    iterator.set(buildUrl ? fileUrlConverter.buildUrl(text) : fileUrlConverter.toPath(text));
                } else {
                    convert(item, buildUrl);
                }
            }
            return value;
        }
        if (value instanceof Collection<?> collection) {
            collection.forEach(item -> convert(item, buildUrl));
        }
        return value;
    }

    private Field findField(Class<?> type, String fieldName) {
        Class<?> current = type;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

}
