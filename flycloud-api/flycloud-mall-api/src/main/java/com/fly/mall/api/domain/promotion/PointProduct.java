package com.fly.mall.api.domain.promotion;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分商城商品 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_point_product")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "积分商城商品对象", description = "积分商城商品表")
public class PointProduct extends BaseEntity {

    @TableId
    private Long id;

    private Long activityId;

    private Long spuId;

    private Long skuId;

    private Integer count;

    private Integer point;

    private Integer price;

    private Integer stock;

    private Integer activityStatus;

}
