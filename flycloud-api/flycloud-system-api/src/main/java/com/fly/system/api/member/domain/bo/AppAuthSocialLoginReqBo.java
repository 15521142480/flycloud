package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员社交登录请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AppAuthSocialLoginReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "社交平台的类型不能为空")
    private Integer type;

    @NotEmpty(message = "授权码不能为空")
    private String code;

    @NotEmpty(message = "state 不能为空")
    private String state;

}
