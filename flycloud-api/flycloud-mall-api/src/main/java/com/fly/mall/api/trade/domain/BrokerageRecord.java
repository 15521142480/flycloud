package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 佣金记录 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "trade_brokerage_record")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "佣金记录对象", description = "佣金记录表")
public class BrokerageRecord extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private String bizId;

    private Integer bizType;

    private String title;

    private String description;

    private Integer price;

    private Integer totalPrice;

    private Integer status;

    private Integer frozenDays;

    private LocalDateTime unfreezeTime;

    private Integer sourceUserLevel;

    private Long sourceUserId;

}
