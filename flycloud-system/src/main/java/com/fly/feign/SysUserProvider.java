package com.fly.feign;

import com.fly.common.constant.ProviderConstants;
import com.fly.common.model.R;
import com.fly.system.api.domain.common.UserInfo;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.feign.ISysUserProvider;
import com.fly.system.service.ISysMenuService;
import com.fly.system.service.ISysRoleService;
import com.fly.system.service.ISysUserService;
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
    private final ISysRoleService sysRoleService;
    private final ISysMenuService sysMenuService;



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
    @GetMapping(ProviderConstants.PROVIDER_USER_USERNAME)
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

    /**
     * 组装通用登录用户数据（权限/菜单等）
     *
     * @param sysUser 系统用户信息
    */
    public UserInfo getUserInfo(SysUser sysUser) {

        if (sysUser == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        userInfo.setUserName(sysUser.getAccount()); // 用户名

        // 权限
        userInfo.setPermissionList(sysRoleService.getPermissionListByUserId(sysUser.getId()));
        userInfo.setRoleIdList(sysRoleService.getRoleIdListByUserId(sysUser.getId()));

        // 菜单
        userInfo.setMenuTreeList(sysMenuService.getMenuTreeListByUserId(sysUser.getId()));

        log.debug("feign调用：userInfo:{}", userInfo);
        return userInfo;
    }
}

