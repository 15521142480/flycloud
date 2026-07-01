package com.fly.system.api.pay.domain.vo;

import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付应用分页项响应视图对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayAppPageItemRespVo extends PayAppRespVo {
    private static final long serialVersionUID = 1L;

    /**
     * 已启用的支付渠道编码。
     */
    private Set<String> channelCodes;
}
