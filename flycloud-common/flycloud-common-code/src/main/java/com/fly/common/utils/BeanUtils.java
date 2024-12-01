package com.fly.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.collection.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * bean工具(基于 cglib 性能优异)
 * <p>
 * 重点 cglib 不支持 拷贝到链式对象
 * 例如: 源对象 拷贝到 目标(链式对象)
 * 请区分好`浅拷贝`和`深拷贝`再做使用
 *
 * @author lxs
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {


    public static <T> T toBean(Object source, Class<T> targetClass) {
        return BeanUtil.toBean(source, targetClass);
    }

    public static <T> T toBean(Object source, Class<T> targetClass, Consumer<T> peek) {
        T target = toBean(source, targetClass);
        if (target != null) {
            peek.accept(target);
        }
        return target;
    }

    public static <S, T> List<T> toBean(List<S> source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        return CollectionUtils.convertList(source, s -> toBean(s, targetType));
    }

    /**
     * 分页
     */
    public static <S, T> PageVo<T> toBean(PageVo<S> source, Class<T> targetType) {
        return toBean(source, targetType, null);
    }
    public static <S, T> PageVo<T> toBean(PageVo<S> source, Class<T> targetType, Consumer<T> peek) {
        if (source == null) {
            return null;
        }
        List<T> list = toBean(source.getList(), targetType);
        if (peek != null) {
            list.forEach(peek);
        }
        return new PageVo<>(list, source.getTotal());
    }


    public static void copyProperties(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtil.copyProperties(source, target, false);
    }


    /**
     * 单对象基于class创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, Class<V> desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        final V target = ReflectUtil.newInstanceIfPossible(desc);
        return copy(source, target);
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, V desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(source.getClass(), desc.getClass(), null);
        beanCopier.copy(source, desc, null);
        return desc;
    }

    /**
     * 列表对象基于class创建拷贝
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> copyList(List<T> sourceList, Class<V> desc) {
        if (ObjectUtil.isNull(sourceList)) {
            return null;
        }
        if (CollUtil.isEmpty(sourceList)) {
            return CollUtil.newArrayList();
        }
        return StreamUtils.toList(sourceList, source -> {
            V target = ReflectUtil.newInstanceIfPossible(desc);
            copy(source, target);
            return target;
        });
    }

    /**
     * bean拷贝到map
     *
     * @param bean 数据来源实体
     * @return map对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> copyToMap(T bean) {
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        return BeanMap.create(bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(beanClass)) {
            return null;
        }
        T bean = ReflectUtil.newInstanceIfPossible(beanClass);
        return mapToBean(map, bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map  数据来源
     * @param bean bean对象
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        BeanMap.create(bean).putAll(map);
        return bean;
    }

    /**
     * BeanCopier属性缓存<br>
     * 缓存用于防止多次反射造成的性能问题
     *
     * @author lxs
     */
    public enum BeanCopierCache {
        /**
         * BeanCopier属性缓存单例
         */
        INSTANCE;

        private final SimpleCache<String, BeanCopier> cache = new SimpleCache<>();

        /**
         * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return Map中对应的BeanCopier
         */
        public BeanCopier get(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final String key = genKey(srcClass, targetClass, converter);
            return cache.get(key, () -> BeanCopier.create(srcClass, targetClass, converter != null));
        }

        /**
         * 获得类与转换器生成的key
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return 属性名和Map映射的key
         */
        private String genKey(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final StringBuilder key = StrUtil.builder()
                .append(srcClass.getName()).append('#').append(targetClass.getName());
            if(null != converter){
                key.append('#').append(converter.getClass().getName());
            }
            return key.toString();
        }
    }

    /**
     * 解决BeanUtils.copyProperties复制null属性的问题
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
