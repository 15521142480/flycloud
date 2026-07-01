package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端 - 钱包流水返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 钱包流水返回对象")
public class AppPayWalletTransactionRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "业务类型")
    private Integer bizType;

    @Schema(description = "交易金额，单位：分")
    private Integer price;

    @Schema(description = "流水标题")
    private String title;

    @Schema(description = "交易时间")
    private LocalDateTime createTime;

}
