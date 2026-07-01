package com.fly.system.api.pay.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Data;

/**
 * 支付渠道响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannelRespVo extends PayChannelVo {
    private static final long serialVersionUID = 1L;
}
