package com.fly.member.feign;

import com.fly.common.domain.model.R;
import com.fly.member.service.IMemberUserService;
import com.fly.system.api.constants.MemberFeignApiConstants;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import com.fly.system.api.member.feign.IMemberUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 系统内部接口-member用户-控制层
 *
 * @author lxs
 * @date 2026/7/4
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberUserApiController implements IMemberUserApi {

    private final IMemberUserService memberUserService;


    /**
     * 根据用户id查询member用户信息
     */
    @Override
    @GetMapping(MemberFeignApiConstants.PROVIDER_MEMBER_USER_ID)
    public R<MemberUserVo> getMemberUserById(Long id) {

        MemberUserVo userVo = memberUserService.queryById(id);
        return R.ok(userVo);
    }


    /**
     * 根据用户ids查询member用户列表
     */
    @Override
    @GetMapping(MemberFeignApiConstants.PROVIDER_MEMBER_USER_IDS)
    public R<List<MemberUserVo>> getMemberUserListByIds(Collection<Long> ids) {

        List<MemberUserVo> userVoList = memberUserService.queryByIds(ids);
        return R.ok(userVoList);
    }

    /**
     * 根据用户手机号查询member用户信息
     */
    @Override
    @GetMapping(MemberFeignApiConstants.PROVIDER_MEMBER_USER_MOBILE)
    public R<MemberUserVo> getMemberUserByMobile(String mobile) {

        MemberUserVo userVo = memberUserService.queryByMobile(mobile);
        return R.ok(userVo);
    }



    /**
     * 根据ids验证member用户
     */
//    @Override
//    @GetMapping(MemberFeignApiConstants.PROVIDER_MEMBER_USER_VALID_IDS)
//    public R<Boolean> validateDeptByIds(Collection<Long> ids) {
//        return R.result(memberUserService.validateDeptByIds(ids));
//    }


    // ====================================================================



}

