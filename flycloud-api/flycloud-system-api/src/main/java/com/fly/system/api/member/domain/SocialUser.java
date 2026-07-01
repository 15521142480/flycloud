package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社交用户。
 *
 * @author lxs
 * @date 2026-07-01
 */
@TableName("system_social_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialUser extends BaseEntity {

    @TableId
    private Long id;

    /**
     * 社交平台类型。
     */
    private Integer type;

    /**
     * 社交 openid。
     */
    private String openid;

    /**
     * 社交 token。
     */
    private String token;

    /**
     * 原始 token 信息。
     */
    private String rawTokenInfo;

    /**
     * 用户昵称。
     */
    private String nickname;

    /**
     * 用户头像。
     */
    private String avatar;

    /**
     * 原始用户信息。
     */
    private String rawUserInfo;

    /**
     * 最近一次授权码。
     */
    private String code;

    /**
     * 最近一次 state。
     */
    private String state;
}
