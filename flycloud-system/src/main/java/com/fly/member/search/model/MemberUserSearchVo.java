package com.fly.member.search.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员用户 ES 查询响应。
 */
@Data
public class MemberUserSearchVo {

    /**
     * 会员用户主键。
     */
    private Long id;

    /**
     * 会员手机号。
     */
    private String mobile;

    /**
     * 会员邮箱。
     */
    private String email;

    /**
     * 会员账号状态。
     */
    private Integer status;

    /**
     * 会员昵称。
     */
    private String nickname;

    /**
     * 会员真实姓名。
     */
    private String name;

    /**
     * 会员头像地址。
     */
    private String avatar;

    /**
     * 会员标签编号集合。
     */
    private List<Long> tagIds;

    /**
     * 会员岗位编号集合。
     */
    private List<Long> postIds;

    /**
     * 会员等级编号。
     */
    private Long levelId;

    /**
     * 会员分组编号。
     */
    private Long groupId;

    /**
     * 会员备注。
     */
    private String mark;

    /**
     * 会员创建时间。
     */
    private LocalDateTime createTime;

    /**
     * 会员最后修改时间。
     */
    private LocalDateTime updateTime;
}
