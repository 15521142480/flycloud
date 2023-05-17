package com.fly.feign;

import com.fly.api.flycloud_system.domain.UserAuthInfo;
import com.fly.api.flycloud_system.feign.SysUserFeignClient;
import com.fly.common.model.R;
import com.fly.user.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户内部接口-控制层
 *
 * @author lxs
 * @date 2023/5/2
 */
@RestController
@RequiredArgsConstructor
public class SysUserFeignController implements SysUserFeignClient {


    private final ISysUserService sysUserService;


    /**
     * 根据系统账号获取系统用户信息
     */
    @Override
    public R<UserAuthInfo> getUserInfoByName(String username) {
        return R.ok(sysUserService.getUserInfoByName(username));
    }


}

