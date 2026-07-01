package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员取消社交绑定请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppSocialUserUnbindReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "社交平台的类型不能为空")
    private Integer type;

    @NotEmpty(message = "社交用户 openid 不能为空")
    private String openid;

}
