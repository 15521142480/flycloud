package com.fly.common.validate;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.core.ArrayValuable;
import com.fly.common.core.IntArrayValuable;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InEnumCollectionValidator implements ConstraintValidator<InEnum, Collection<Integer>> {

//    private List<Integer> values;

    private List<?> values;

    @Override
    public void initialize(InEnum annotation) {
        ArrayValuable<?>[] values = annotation.value().getEnumConstants();
        if (values.length == 0) {
            this.values = Collections.emptyList();
        } else {
            this.values = Arrays.asList(values[0].array());
        }
    }

    @Override
    public boolean isValid(Collection<Integer> list, ConstraintValidatorContext context) {
        // 校验通过
        if (CollUtil.containsAll(values, list)) {
            return true;
        }
        // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
        context.disableDefaultConstraintViolation(); // 禁用默认的 message 的值
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
                .replaceAll("\\{value}", CollUtil.join(list, ","))).addConstraintViolation(); // 重新添加错误提示语句
        return false;
    }

}

