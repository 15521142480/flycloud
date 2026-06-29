package com.fly.mall.api.domain.trade.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 交易订单创建返回对象。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Data
@Schema(description = "移动端 - 交易订单创建返回对象")
public class AppTradeOrderCreateRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    private Long id;

    @Schema(description = "支付订单编号")
    private Long payOrderId;

}
