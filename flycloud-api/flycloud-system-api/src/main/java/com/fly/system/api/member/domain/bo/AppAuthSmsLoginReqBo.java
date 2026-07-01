package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 移动端会员短信登录请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AppAuthSmsLoginReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @NotEmpty(message = "手机验证码不能为空")
    @Length(min = 4, max = 6, message = "手机验证码长度为 4-6 位")
    private String code;

    private Integer socialType;

    private String socialCode;

    private String socialState;

}
