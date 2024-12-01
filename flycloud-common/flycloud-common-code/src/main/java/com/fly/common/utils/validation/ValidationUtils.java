package com.fly.common.utils.validation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.fly.common.utils.spring.SpringUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 */
public class ValidationUtils {

    private static final Pattern PATTERN_MOBILE = Pattern.compile("^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[0,1,4-9])|(?:5[0-3,5-9])|(?:6[2,5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[0-3,5-9]))\\d{8}$");

    private static final Pattern PATTERN_URL = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private static final Pattern PATTERN_XML_NCNAME = Pattern.compile("[a-zA-Z_][\\-_.0-9_a-zA-Z$]*");

    private static final Validator VALID = SpringUtils.getBean(Validator.class);



    public static boolean isMobile(String mobile) {
        return StringUtils.hasText(mobile)
                && PATTERN_MOBILE.matcher(mobile).matches();
    }

    public static boolean isURL(String url) {
        return StringUtils.hasText(url)
                && PATTERN_URL.matcher(url).matches();
    }

    public static boolean isXmlNCName(String str) {
        return StringUtils.hasText(str)
                && PATTERN_XML_NCNAME.matcher(str).matches();
    }

//    public static void validate(Object object, Class<?>... groups) {
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Assert.notNull(validator);
//        validate(validator, object, groups);
//    }
//
//    public static void validate(Validator validator, Object object, Class<?>... groups) {
//        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
//        if (CollUtil.isNotEmpty(constraintViolations)) {
//            throw new ConstraintViolationException(constraintViolations);
//        }
//    }


    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = VALID.validate(object, groups);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException("参数校验异常", validate);
        }
    }



}
