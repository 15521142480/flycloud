package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付钱包流水返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "支付钱包流水返回对象")
public class PayWalletTransactionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String no;

    private Long walletId;

    private Integer bizType;

    private String bizId;

    private String title;

    private Integer price;

    private Integer balance;

    private LocalDateTime createTime;

}
