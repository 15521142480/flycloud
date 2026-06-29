package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员签到记录视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class MemberSignInRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Integer day;
    private Integer point;
    private Integer experience;
    private LocalDateTime createTime;
}
