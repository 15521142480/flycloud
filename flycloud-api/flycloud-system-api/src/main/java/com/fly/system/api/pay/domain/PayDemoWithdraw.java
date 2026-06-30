package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付示例提现单。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_demo_withdraw")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayDemoWithdraw extends BaseEntity {

    @TableId
    private Long id;

    private String subject;

    private Integer price;

    private String userAccount;

    private String userName;

    private Integer type;

    private Integer status;

    private Long payTransferId;

    private String transferChannelCode;

    private LocalDateTime transferTime;

    private String transferErrorMsg;

}
