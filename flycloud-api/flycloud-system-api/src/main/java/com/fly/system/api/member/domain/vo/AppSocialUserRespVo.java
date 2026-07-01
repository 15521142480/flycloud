package com.fly.system.api.member.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员社交用户响应对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AppSocialUserRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openid;

    private String nickname;

    private String avatar;

}
