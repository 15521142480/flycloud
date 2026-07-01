package com.fly.system.api.member.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端会员登录响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppAuthLoginRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime expiresTime;

    private Long expiresIn;

    private String openid;

}
