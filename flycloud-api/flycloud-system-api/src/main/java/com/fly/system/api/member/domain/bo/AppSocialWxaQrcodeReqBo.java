package com.fly.system.api.member.domain.bo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员获取微信小程序码请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AppSocialWxaQrcodeReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String scene;

    @NotEmpty(message = "页面路径不能为空")
    private String path;

    private Integer width;

    private Boolean autoColor;

    private Boolean checkPath;

    private Boolean hyaline;

}
