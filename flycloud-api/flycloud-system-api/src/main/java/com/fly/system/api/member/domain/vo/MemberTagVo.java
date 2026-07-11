package com.fly.system.api.member.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员标签视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberTagVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonLongId
    private Long id;
    private String name;
    private Boolean isDeleted;
    private LocalDateTime createTime;
}
