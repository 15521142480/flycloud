package com.fly.system.api.member.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台 - 会员签到规则响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberSignInConfigRespVo extends MemberSignInConfigVo {
    private static final long serialVersionUID = 1L;
}
