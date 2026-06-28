package com.fly.mall.api.domain.trade;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 售后日志 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_after_sale_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "售后日志对象", description = "售后日志表")
public class AfterSaleLog extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Integer userType;

    private Long afterSaleId;

    private Integer beforeStatus;

    private Integer afterStatus;

    private Integer operateType;

    private String content;

}
