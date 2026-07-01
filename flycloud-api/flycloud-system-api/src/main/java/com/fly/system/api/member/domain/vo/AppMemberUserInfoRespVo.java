package com.fly.system.api.member.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端会员用户个人信息响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class AppMemberUserInfoRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nickname;

    private String avatar;

    private String mobile;

    private String email;

    private Integer sex;

    private Integer point;

    private Integer experience;

    private Level level;

    private Boolean brokerageEnabled;

    /**
     * 移动端会员等级摘要。
     *
     * @author lxs
     * @date 2026-07-01
     */
    @Data
    public static class Level implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;

        private String name;

        private Integer level;

        private String icon;

    }

}
