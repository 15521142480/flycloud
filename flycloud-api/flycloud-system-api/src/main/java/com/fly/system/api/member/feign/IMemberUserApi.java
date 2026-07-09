package com.fly.system.api.member.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.member.path.MemberApiPaths;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * member用户调用类
 * <p>
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "MemberApi")
public interface IMemberUserApi {


    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return Result
     */
    @GetMapping(MemberApiPaths.PROVIDER_MEMBER_USER_ID)
    R<MemberUserVo> getMemberUserById(@RequestParam("id") Long id);

    /**
     * 根据ids查询用户列表
     *
     * @param ids　用户名
     * @return Result
     */
    @GetMapping(MemberApiPaths.PROVIDER_MEMBER_USER_IDS)
    R<List<MemberUserVo>> getMemberUserListByIds(@RequestParam("ids") Collection<Long> ids);


    /**
     * 根据id查询用户信息
     *
     * @param mobile
     * @return Result
     */
    @GetMapping(MemberApiPaths.PROVIDER_MEMBER_USER_MOBILE)
    R<MemberUserVo> getMemberUserByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据ids验证用户
     *
     * @param ids　用户ids
     * @return Result
     */
//    @GetMapping(MemberFeignApiConstants.PROVIDER_MEMBER_USER_VALID_IDS)
//    R<Boolean> validateDeptByIds(@RequestParam("ids") Collection<Long> ids);


    // ==========================================================================

    /**
     * 获得用户 Map
     *
     * @param ids 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, MemberUserVo> getMemberUserMapByIds(Collection<Long> ids) {

        List<MemberUserVo> users = this.getMemberUserListByIds(ids).getCheckedData();
        return CollectionUtils.convertMap(users, MemberUserVo::getId);
    }


}
