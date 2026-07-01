package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 移动端会员校验短信验证码请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AppAuthSmsValidateReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mobile;

    @NotNull(message = "发送场景不能为空")
    private Integer scene;

    @NotEmpty(message = "手机验证码不能为空")
    @Length(min = 4, max = 6, message = "手机验证码长度为 4-6 位")
    private String code;

}
