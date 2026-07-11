package com.fly.system.api.member.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员等级记录视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberLevelRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonLongId
    private Long id;
    @JsonLongId
    private Long userId;
    @JsonLongId
    private Long levelId;
    private Integer level;
    private Integer discountPercent;
    private Integer experience;
    private Integer userExperience;
    private String remark;
    private String description;
    private LocalDateTime createTime;
}
