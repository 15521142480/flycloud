package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付示例提现单视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayDemoWithdrawVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String subject;

    private Integer price;

    private String userAccount;

    private String userName;

    private Integer type;

    private Integer status;

    @JsonLongId
    private Long payTransferId;

    private String transferChannelCode;

    private LocalDateTime transferTime;

    private String transferErrorMsg;

    private LocalDateTime createTime;

}
