package com.fly.test.member.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/** 会员用户 ES 查询响应。 */
@Data
public class MemberUserSearchVo {

    private Long id;
    private String mobile;
    private String email;
    private Integer status;
    private String nickname;
    private String name;
    private String avatar;
    private List<Long> tagIds;
    private List<Long> postIds;
    private Long levelId;
    private Long groupId;
    private String mark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
