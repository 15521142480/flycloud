package com.fly.system.api.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.constants.SystemFeignApiConstants;
import com.fly.system.api.domain.common.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fly.system.api.domain.SysUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户调用类
 * <p>
 *
 * 1. 接口路径如果跟controller层一样的话回报路径冲突；解决1: 此处与前缀（/provider）不分层，在controller分层，方法2:@FeignClient使用path参数赋值前缀（/provider）
 * 2. 每个@FeignClient注解对应的接口会导致FeignClientSpecification重复注入，使用contextId解决
 * 3. 突然忘了，想起来再写
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysUserApi")
public interface ISysUserApi {

    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_ID)
    R<SysUser> getUserById(@RequestParam("id") Long id);

    /**
     * 根据ids查询用户列表
     * @param ids　用户名
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_IDS)
    R<List<SysUser>> getUserListByIds(@RequestParam("ids") Collection<Long> ids);

    /**
     * 根据userName查询用户信息
     * @param userName　用户名
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_USERNAME)
    R<UserInfo> getUserByUserName(@RequestParam("userName") String userName);

    /**
     * 根据手机号查询用户信息
     * @param mobile　手机号码
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_MOBILE)
    R<UserInfo> getUserByMobile(@RequestParam("mobile") String mobile);


    /**
     * 根据ids验证用户
     * @param ids　用户ids
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_USER_VALID_IDS)
    R<Boolean> validateDeptByIds(@RequestParam("ids") Collection<Long> ids);


    // ==========================================================================

    /**
     * 获得用户 Map
     *
     * @param ids 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, SysUser> getUserMapByIds(Collection<Long> ids) {

        List<SysUser> users = this.getUserListByIds(ids).getCheckedData();
        return CollectionUtils.convertMap(users, SysUser::getId);
    }


}
