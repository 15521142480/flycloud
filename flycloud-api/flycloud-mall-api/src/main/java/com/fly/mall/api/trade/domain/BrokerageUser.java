package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分销用户 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "trade_brokerage_user")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "分销用户对象", description = "分销用户表")
public class BrokerageUser extends BaseEntity {

    @TableId
    private Long id;

    private Long bindUserId;

    private LocalDateTime bindUserTime;

    private Boolean brokerageEnabled;

    private LocalDateTime brokerageTime;

    private Integer brokeragePrice;

    private Integer frozenPrice;

}
