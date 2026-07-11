package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 交易订单创建返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 交易订单创建返回对象")
public class AppTradeOrderCreateRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @JsonLongId
    private Long id;

    @Schema(description = "支付订单编号")
    @JsonLongId
    private Long payOrderId;

}
