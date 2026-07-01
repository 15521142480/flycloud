package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员微信小程序登录请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppAuthWeixinMiniAppLoginReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "手机 code 不能为空")
    private String phoneCode;

    @NotEmpty(message = "登录 code 不能为空")
    private String loginCode;

    @NotEmpty(message = "state 不能为空")
    private String state;

}
