package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台 - 钱包流水响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "后台 - 钱包流水响应对象")
public class PayWalletTransactionRespVo extends PayWalletTransactionVo {
    private static final long serialVersionUID = 1L;
}
