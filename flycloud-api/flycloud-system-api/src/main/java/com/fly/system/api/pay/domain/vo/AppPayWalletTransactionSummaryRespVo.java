package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 钱包流水统计返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 钱包流水统计返回对象")
public class AppPayWalletTransactionSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "累计支出，单位：分")
    private Integer totalExpense;

    @Schema(description = "累计收入，单位：分")
    private Integer totalIncome;

}
