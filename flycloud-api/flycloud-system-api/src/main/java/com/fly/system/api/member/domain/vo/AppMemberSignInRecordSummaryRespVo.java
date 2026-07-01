package com.fly.system.api.member.domain.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 移动端会员签到统计视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppMemberSignInRecordSummaryRespVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer totalDay;
    private Integer continuousDay;
    private Boolean todaySignIn;
}
