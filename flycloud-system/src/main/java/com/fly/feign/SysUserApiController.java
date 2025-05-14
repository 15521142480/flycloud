package com.fly.feign;

import com.fly.system.api.constants.SystemFeignApiConstants;
import com.fly.common.domain.model.R;
import com.fly.system.api.domain.common.UserInfo;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysUserApi;
import com.fly.system.service.ISysRoleService;
import com.fly.system.service.ISysUserRoleService;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 系统内部接口-用户-控制层
 *
 * @author lxs
 * @date 2023/5/2
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysUserApiController implements ISysUserApi {


    private final ISysUserService sysUserService;

    private final ISysRoleService sysRoleService;
    private final ISysUserRoleService sysUserRoleService;



    /**
     * 根据用户id查询用户信息
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_ID)
    public R<SysUserVo> getUserById(Long id) {

        SysUserVo sysUserVo = sysUserService.queryById(id);
        // 测试日志埋点
//        TrackUtil.info(ISysUserProvider.class.getName(), "userInfoById", JSON.toJSONString(sysUserVo));
        return R.ok(sysUserVo);
    }


    /**
     * 根据用户ids查询用户列表
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_IDS)
    public R<List<SysUserVo>> getUserListByIds(Collection<Long> ids) {

        List<SysUserVo> sysUserList = sysUserService.getByIds(ids);
        return R.ok(sysUserList);
    }


    /**
     * 根据用户名查询用户信息
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_USERNAME)
    public R<UserInfo> getUserByUserName(String userName) {

        SysUser sysUser = sysUserService.queryOneUserByUser(new SysUser().setAccount(userName));
        return R.ok(this.getUserInfo(sysUser));
    }


    /**
     * 根据用户手机号查询用户信息
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_MOBILE)
    public R<UserInfo> getUserByMobile(String mobile) {

        SysUser sysUser = sysUserService.queryOneUserByUser(new SysUser().setTelephone(mobile));
        return R.ok(getUserInfo(sysUser));
    }

    /**
     * 根据ids验证用户
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_VALID_IDS)
    public R<Boolean> validateDeptByIds(Collection<Long> ids) {
        return R.ok(sysUserService.validateDeptByIds(ids));
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
        userInfo.setRoleIds(String.join(",", sysUserRoleService.getRoleIdListByUserId(sysUser.getId())));

        log.debug("feign调用：userInfo:{}", userInfo);
        return userInfo;
    }
}

