package com.fly.common.utils.collection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.fly.common.utils.collection.CollectionUtils.convertList;

/**
 * Array 工具类
 *
 */
public class ArrayUtils {

    /**
     * 将 object 和 newElements 合并成一个数组
     *
     * @param object 对象
     * @param newElements 数组
     * @param <T> 泛型
     * @return 结果数组
     */
    @SafeVarargs
    public static <T> Consumer<T>[] append(Consumer<T> object, Consumer<T>... newElements) {
        if (object == null) {
            return newElements;
        }
        Consumer<T>[] result = ArrayUtil.newArray(Consumer.class, 1 + newElements.length);
        result[0] = object;
        System.arraycopy(newElements, 0, result, 1, newElements.length);
        return result;
    }

    public static <T, V> V[] toArray(Collection<T> from, Function<T, V> mapper) {
        return toArray(CollectionUtils.convertList(from, mapper));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> from) {
        if (CollectionUtil.isEmpty(from)) {
            return (T[]) (new Object[0]);
        }
        return ArrayUtil.toArray(from, (Class<T>) IterUtil.getElementType(from.iterator()));
    }

    public static <T> T get(T[] array, int index) {
        if (null == array || index >= array.length) {
            return null;
        }
        return array[index];
    }

    /***
     * 去除String数组中的空值
     */
    public static String[] deleteArrayNull(String[] string) {

        // 赋值新数组
        ArrayList<String> strList = new ArrayList<>();
        Collections.addAll(strList, string);

        // 删除list列表中所有的空值
        while (strList.remove(null));
        while (strList.remove(""));

        // 把list列表转换给一个新定义的中间数组，并赋值给它
        String[] strArrLast = strList.toArray(new String[strList.size()]);

        return strArrLast;
    }


}
