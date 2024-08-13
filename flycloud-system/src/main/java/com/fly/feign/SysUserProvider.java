package com.fly.feign;

import cn.hutool.core.collection.ListUtil;
import com.fly.common.constant.ProviderConstants;
import com.fly.common.model.R;
import com.fly.system.service.ISysRolePermissionService;
import com.fly.system.service.ISysUserService;
import com.flycloud.system.api.dto.UserInfo;
import com.flycloud.system.api.entity.SysUser;
import com.flycloud.system.api.feign.ISysUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户内部接口-控制层
 *
 * @author lxs
 * @date 2023/5/2
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysUserProvider implements ISysUserProvider {


    private final ISysUserService sysUserService;
    private final ISysRolePermissionService sysRolePermissionService;



    /**
     * 根据用户id查询用户信息
     */
    @Override
    @GetMapping(ProviderConstants.PROVIDER_USER_ID)
    public R<SysUser> getUserById(Long id) {

        SysUser sysUser = sysUserService.getById(id);
        // 测试日志埋点
//        TrackUtil.info(ISysUserProvider.class.getName(), "userInfoById", JSON.toJSONString(sysUser));
        return R.ok(sysUser);
    }


    /**
     * 根据用户名查询用户信息
     */
    @Override
    @GetMapping(ProviderConstants.PROVIDER_USER_MOBILE)
    public R<UserInfo> getUserByUserName(String userName) {

        SysUser sysUser = sysUserService.getOneIgnoreTenant(new SysUser().setAccount(userName));
        return R.ok(this.getUserInfo(sysUser));
    }


    /**
     * 根据用户手机号查询用户信息
     */
    @Override
    @GetMapping(ProviderConstants.PROVIDER_USER_MOBILE)
    public R<UserInfo> getUserByMobile(String mobile) {

        SysUser sysUser = sysUserService.getOneIgnoreTenant(new SysUser().setTelephone(mobile));
        return R.ok(getUserInfo(sysUser));
    }



    // ====================================================================

    public UserInfo getUserInfo(SysUser sysUser) {

        if (sysUser == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
//        userInfo.setPermissions(sysRolePermissionService.getMenuIdByRoleId(sysUser.getRoleId()));
        userInfo.setRoleIds(ListUtil.toList(sysUser.getRoleId()));
        userInfo.setTenantId(sysUser.getTenantId());

        log.debug("feign调用：userInfo:{}", userInfo);
        return userInfo;
    }
}

