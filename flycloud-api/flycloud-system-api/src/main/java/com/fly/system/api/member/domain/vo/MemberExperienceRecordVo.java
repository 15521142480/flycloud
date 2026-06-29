package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员经验记录视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class MemberExperienceRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Integer bizType;
    private String bizId;
    private String title;
    private String description;
    private Integer experience;
    private Integer totalExperience;
    private LocalDateTime createTime;
}
