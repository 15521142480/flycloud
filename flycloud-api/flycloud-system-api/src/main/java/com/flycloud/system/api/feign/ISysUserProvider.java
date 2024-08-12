package com.flycloud.system.api.feign;

import com.fly.common.constant.ProviderConstants;
import com.fly.common.constant.ServerNames;
import com.fly.common.model.R;
import com.flycloud.system.api.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.flycloud.system.api.entity.SysUser;

/**
 * 用户调用类
 *
 * @author pangu
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME)
public interface ISysUserProvider {

    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return Result
     */
    @GetMapping(ProviderConstants.PROVIDER_USER_ID)
    R<SysUser> getUserById(@RequestParam("id") Long id);

    /**
     * 根据userName查询用户信息
     * @param userName　用户名
     * @return Result
     */
    @GetMapping(ProviderConstants.PROVIDER_USER_USERNAME)
    R<UserInfo> getUserByUserName(@RequestParam("userName") String userName);

    /**
     * 根据手机号查询用户信息
     * @param mobile　手机号码
     * @return Result
     */
    @GetMapping(ProviderConstants.PROVIDER_USER_MOBILE)
    R<UserInfo> getUserByMobile(@RequestParam("mobile") String mobile);

}
