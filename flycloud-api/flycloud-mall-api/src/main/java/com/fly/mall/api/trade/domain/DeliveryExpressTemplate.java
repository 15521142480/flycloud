package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运费模板 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_delivery_express_template")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "运费模板对象", description = "运费模板表")
public class DeliveryExpressTemplate extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private Integer chargeMode;

    private Integer sort;

}
