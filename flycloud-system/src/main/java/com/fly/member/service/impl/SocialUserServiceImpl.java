package com.fly.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.StringUtils;
import com.fly.member.mapper.SocialUserBindMapper;
import com.fly.member.mapper.SocialUserMapper;
import com.fly.member.service.ISocialUserService;
import com.fly.system.api.member.domain.SocialUser;
import com.fly.system.api.member.domain.SocialUserBind;
import com.fly.system.api.member.domain.bo.AppSocialUserBindReqBo;
import com.fly.system.api.member.domain.bo.AppSocialUserUnbindReqBo;
import com.fly.system.api.member.domain.vo.AppSocialUserRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * 社交用户 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class SocialUserServiceImpl implements ISocialUserService {

    private final SocialUserMapper socialUserMapper;
    private final SocialUserBindMapper socialUserBindMapper;

    /**
     * 通过授权码获得或创建社交用户。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SocialUser getOrCreateSocialUser(Integer type, String code, String state) {
        if (type == null || StringUtils.isBlank(code)) {
            throw new ServiceException("社交平台类型和授权码不能为空");
        }
        String openid = buildOpenid(type, code, state);
        SocialUser socialUser = selectByTypeAndOpenid(type, openid);
        LocalDateTime now = LocalDateTime.now();
        if (socialUser != null) {
            SocialUser updateUser = new SocialUser();
            updateUser.setId(socialUser.getId());
            updateUser.setCode(code);
            updateUser.setState(state);
            updateUser.setUpdateBy("social");
            updateUser.setUpdateTime(now);
            socialUserMapper.updateById(updateUser);
            socialUser.setCode(code);
            socialUser.setState(state);
            return socialUser;
        }

        SocialUser createUser = new SocialUser();
        createUser.setType(type);
        createUser.setOpenid(openid);
        createUser.setNickname("社交用户" + openid.substring(Math.max(0, openid.length() - 6)));
        createUser.setAvatar("");
        createUser.setCode(code);
        createUser.setState(state);
        createUser.setIsDeleted(false);
        createUser.setCreateBy("social");
        createUser.setCreateTime(now);
        createUser.setUpdateBy("social");
        createUser.setUpdateTime(now);
        socialUserMapper.insert(createUser);
        return createUser;
    }

    /**
     * 通过授权码获得绑定的社交用户。
     */
    @Override
    public SocialUser getSocialUserByCode(Integer userType, Integer type, String code, String state) {
        SocialUser socialUser = getOrCreateSocialUser(type, code, state);
        SocialUserBind bind = selectBindBySocialUserId(userType, socialUser.getId());
        if (bind == null) {
            return socialUser;
        }
        socialUser.getParams().put("bindUserId", bind.getUserId());
        return socialUser;
    }

    /**
     * 绑定社交用户。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bindSocialUser(Long userId, Integer userType, AppSocialUserBindReqBo reqBo) {
        if (userId == null) {
            throw new ServiceException("用户未登录");
        }
        SocialUser socialUser = getOrCreateSocialUser(reqBo.getType(), reqBo.getCode(), reqBo.getState());
        SocialUserBind existsBind = selectBindBySocialUserId(userType, socialUser.getId());
        if (existsBind != null && !Objects.equals(existsBind.getUserId(), userId)) {
            throw new ServiceException("该社交账号已绑定其它用户");
        }
        if (existsBind != null) {
            return socialUser.getOpenid();
        }
        SocialUserBind sameTypeBind = selectBindByUserAndType(userType, userId, reqBo.getType());
        if (sameTypeBind != null) {
            socialUserBindMapper.deleteById(sameTypeBind.getId());
        }

        LocalDateTime now = LocalDateTime.now();
        SocialUserBind bind = new SocialUserBind();
        bind.setUserId(userId);
        bind.setUserType(userType);
        bind.setSocialUserId(socialUser.getId());
        bind.setSocialType(reqBo.getType());
        bind.setIsDeleted(false);
        bind.setCreateBy(String.valueOf(userId));
        bind.setCreateTime(now);
        bind.setUpdateBy(String.valueOf(userId));
        bind.setUpdateTime(now);
        socialUserBindMapper.insert(bind);
        return socialUser.getOpenid();
    }

    /**
     * 取消绑定社交用户。
     */
    @Override
    public Boolean unbindSocialUser(Long userId, Integer userType, AppSocialUserUnbindReqBo reqBo) {
        SocialUser socialUser = selectByTypeAndOpenid(reqBo.getType(), reqBo.getOpenid());
        if (socialUser == null) {
            return true;
        }
        SocialUserBind bind = selectBindBySocialUserId(userType, socialUser.getId());
        if (bind == null || !Objects.equals(bind.getUserId(), userId)) {
            return true;
        }
        socialUserBindMapper.deleteById(bind.getId());
        return true;
    }

    /**
     * 查询用户已绑定的社交用户。
     */
    @Override
    public AppSocialUserRespVo getSocialUserByUserId(Integer userType, Long userId, Integer type) {
        SocialUserBind bind = selectBindByUserAndType(userType, userId, type);
        if (bind == null) {
            return null;
        }
        SocialUser socialUser = socialUserMapper.selectById(bind.getSocialUserId());
        if (socialUser == null || Boolean.TRUE.equals(socialUser.getIsDeleted())) {
            return null;
        }
        AppSocialUserRespVo respVo = new AppSocialUserRespVo();
        respVo.setOpenid(socialUser.getOpenid());
        respVo.setNickname(socialUser.getNickname());
        respVo.setAvatar(socialUser.getAvatar());
        return respVo;
    }

    private SocialUser selectByTypeAndOpenid(Integer type, String openid) {
        return socialUserMapper.selectOne(Wrappers.<SocialUser>lambdaQuery()
                .eq(SocialUser::getIsDeleted, false)
                .eq(SocialUser::getType, type)
                .eq(SocialUser::getOpenid, openid)
                .last("LIMIT 1"));
    }

    private SocialUserBind selectBindBySocialUserId(Integer userType, Long socialUserId) {
        return socialUserBindMapper.selectOne(Wrappers.<SocialUserBind>lambdaQuery()
                .eq(SocialUserBind::getIsDeleted, false)
                .eq(SocialUserBind::getUserType, userType)
                .eq(SocialUserBind::getSocialUserId, socialUserId)
                .last("LIMIT 1"));
    }

    private SocialUserBind selectBindByUserAndType(Integer userType, Long userId, Integer socialType) {
        return socialUserBindMapper.selectOne(Wrappers.<SocialUserBind>lambdaQuery()
                .eq(SocialUserBind::getIsDeleted, false)
                .eq(SocialUserBind::getUserType, userType)
                .eq(SocialUserBind::getUserId, userId)
                .eq(SocialUserBind::getSocialType, socialType)
                .last("LIMIT 1"));
    }

    private String buildOpenid(Integer type, String code, String state) {
        String raw = type + ":" + code + ":" + (state == null ? "" : state);
        return "local_" + UUID.nameUUIDFromBytes(raw.getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
    }
}
