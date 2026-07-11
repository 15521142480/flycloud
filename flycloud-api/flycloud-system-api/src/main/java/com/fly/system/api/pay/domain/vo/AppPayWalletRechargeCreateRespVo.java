package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 创建钱包充值返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 创建钱包充值返回对象")
public class AppPayWalletRechargeCreateRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "充值编号")
    @JsonLongId
    private Long id;

    @Schema(description = "支付订单编号")
    @JsonLongId
    private Long payOrderId;

}
