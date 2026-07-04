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
        String password = encoder.encode("123456");
        System.out.println("123456的密码为：");
        System.out.println(password);
    }


}
