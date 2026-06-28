package com.fly.mall.api.domain.trade;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购物车 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_cart")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "购物车对象", description = "购物车表")
public class Cart extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Long spuId;

    private Long skuId;

    private Integer count;

    private Boolean selected;

}
