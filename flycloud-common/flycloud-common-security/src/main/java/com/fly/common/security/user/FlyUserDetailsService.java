package com.fly.common.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户详细扩展
 *
 * @author: lxs
 * @date: 2024/8/12
 */
public interface FlyUserDetailsService extends UserDetailsService {


    /**
     * 根据手机号登录
     *
     * @param mobile
     * @return UserDetails
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;


    /**
     * 根据社交账号登录
     *
     * @param openId 第三方的绑定的openId
     */
    UserDetails loadUserBySocial(String openId) throws UsernameNotFoundException;

}
