package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员用户视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class MemberUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String mobile;
    private String email;
    private Integer status;
    private String nickname;
    private String avatar;
    private String name;
    private Integer sex;
    private LocalDateTime birthday;
    private Integer areaId;
    private String mark;
    private Integer point;
    private String tagIds;
    private Long levelId;
    private Integer experience;
    private Long groupId;
    private LocalDateTime createTime;
}
