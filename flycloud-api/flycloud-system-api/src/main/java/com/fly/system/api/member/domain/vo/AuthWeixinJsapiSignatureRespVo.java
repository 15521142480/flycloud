package com.fly.system.api.member.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员微信公众号 JSAPI 签名响应对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AuthWeixinJsapiSignatureRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;

    private String nonceStr;

    private Long timestamp;

    private String url;

    private String signature;

}
