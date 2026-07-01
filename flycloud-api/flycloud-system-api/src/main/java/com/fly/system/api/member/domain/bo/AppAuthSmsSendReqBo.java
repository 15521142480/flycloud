package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员发送短信验证码请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppAuthSmsSendReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mobile;

    @NotNull(message = "发送场景不能为空")
    private Integer scene;

}
