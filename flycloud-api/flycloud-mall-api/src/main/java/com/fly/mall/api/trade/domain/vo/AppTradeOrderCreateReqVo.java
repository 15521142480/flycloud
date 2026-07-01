package com.fly.mall.api.trade.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 移动端 - 交易订单创建请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "移动端 - 交易订单创建请求对象")
public class AppTradeOrderCreateReqVo extends AppTradeOrderSettlementReqVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户备注")
    private String remark;

}
