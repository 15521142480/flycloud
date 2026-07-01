package com.fly.system.api.member.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员小程序订阅模板响应对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class AppSocialWxaSubscribeTemplateRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String content;

    private String example;

    private Integer type;

}
