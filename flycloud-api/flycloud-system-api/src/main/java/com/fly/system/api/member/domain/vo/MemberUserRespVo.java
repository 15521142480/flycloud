package com.fly.system.api.member.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台会员用户响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberUserRespVo extends MemberUserVo {

    private static final long serialVersionUID = 1L;

    /**
     * 注册 IP。
     */
    private String registerIp;

    /**
     * 最后登录 IP。
     */
    private String loginIp;

    /**
     * 最后登录时间。
     */
    private LocalDateTime loginDate;

    /**
     * 所在地名称。
     */
    private String areaName;

    /**
     * 总积分，当前项目没有累计积分字段时使用当前积分兼容。
     */
    private Integer totalPoint;

    /**
     * 会员标签名称列表。
     */
    private List<String> tagNames;

    /**
     * 会员等级名称。
     */
    private String levelName;

    /**
     * 用户分组名称。
     */
    private String groupName;

}
