package com.fly.member.service;

import com.fly.system.api.member.domain.SocialUser;
import com.fly.system.api.member.domain.bo.AppSocialUserBindReqBo;
import com.fly.system.api.member.domain.bo.AppSocialUserUnbindReqBo;
import com.fly.system.api.member.domain.vo.AppSocialUserRespVo;

/**
 * 社交用户 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ISocialUserService {

    /**
     * 通过授权码获得或创建社交用户。
     */
    SocialUser getOrCreateSocialUser(Integer type, String code, String state);

    /**
     * 通过授权码获得绑定的社交用户。
     */
    SocialUser getSocialUserByCode(Integer userType, Integer type, String code, String state);

    /**
     * 绑定社交用户。
     */
    String bindSocialUser(Long userId, Integer userType, AppSocialUserBindReqBo reqBo);

    /**
     * 取消绑定社交用户。
     */
    Boolean unbindSocialUser(Long userId, Integer userType, AppSocialUserUnbindReqBo reqBo);

    /**
     * 查询用户已绑定的社交用户。
     */
    AppSocialUserRespVo getSocialUserByUserId(Integer userType, Long userId, Integer type);
}
