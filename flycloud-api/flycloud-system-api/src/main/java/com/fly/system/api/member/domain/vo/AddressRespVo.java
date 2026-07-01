package com.fly.system.api.member.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台 - 会员收件地址响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressRespVo extends MemberAddressVo {
    private static final long serialVersionUID = 1L;
}
