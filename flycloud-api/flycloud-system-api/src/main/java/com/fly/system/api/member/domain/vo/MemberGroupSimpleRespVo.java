package com.fly.system.api.member.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台 - 会员分组精简响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupSimpleRespVo extends MemberGroupVo {
    private static final long serialVersionUID = 1L;
}
