package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员标签视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class MemberTagVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Boolean isDeleted;
    private LocalDateTime createTime;
}
