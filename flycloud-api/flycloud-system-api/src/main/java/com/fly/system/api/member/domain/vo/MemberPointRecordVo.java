package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员积分记录视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class MemberPointRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String bizId;
    private Integer bizType;
    private String title;
    private String description;
    private Integer point;
    private Integer totalPoint;
    private LocalDateTime createTime;
}
