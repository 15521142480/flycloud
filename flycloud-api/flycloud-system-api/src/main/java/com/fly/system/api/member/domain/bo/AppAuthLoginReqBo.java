package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 移动端会员账号密码登录请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppAuthLoginReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    private Integer socialType;

    private String socialCode;

    private String socialState;

}
