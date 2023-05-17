package com.fly.auth.extend.services;

import com.fly.api.flycloud_system.domain.UserAuthInfo;
import com.fly.api.flycloud_system.feign.SysUserFeignClient;
import com.fly.auth.domain.SysUserDetails;
import com.fly.common.enums.user.UserStatus;
import com.fly.common.exception.user.UserException;
import com.fly.common.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 系统用户登录校验-接口层
 *
 * @author lxs
 * @date 2023/4/28
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailsServices implements UserDetailsService {


    private final SysUserFeignClient sysUserFeignClient;


    /**
     * 根据用户名登录
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        R<UserAuthInfo> resultData = sysUserFeignClient.getUserInfoByName(username);
        UserAuthInfo userAuthInfo = resultData.getData();
        if (userAuthInfo == null) {
            throw new UserException("没有该账号("+username+")信息!");
        }
        if (! userAuthInfo.getStatus().equals(UserStatus.ENABLE.getValue())) {
            throw new DisabledException("该账号("+username+")已被禁用!");
        }

        return new SysUserDetails(userAuthInfo);
    }


}
