package com.fly.auth.utils;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * test
 *
 * @author: lxs
 * @date: 2026/7/4
 */
public class TestAuthUtils {

    public static void main(String[] args) {

        // 加密
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String password = "abc123456";
        String passwordEncode = encoder.encode(password);
        System.out.println(password + "的密码为：" + passwordEncode);
    }


}
