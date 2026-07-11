package com.fly.system.api.member.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员等级视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberLevelVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonLongId
    private Long id;
    private String name;
    private Integer level;
    private Integer experience;
    private Integer discountPercent;
    private String icon;
    private String backgroundUrl;
    private Integer status;
    private Boolean isDeleted;
    private LocalDateTime createTime;
}
