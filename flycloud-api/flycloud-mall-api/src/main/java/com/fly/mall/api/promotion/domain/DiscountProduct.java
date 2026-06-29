package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 限时折扣商品 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_discount_product")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "限时折扣商品对象", description = "限时折扣商品表")
public class DiscountProduct extends BaseEntity {

    @TableId
    private Long id;

    private Long activityId;

    private Long spuId;

    private Long skuId;

    private Integer discountType;

    private Integer discountPercent;

    private Integer discountPrice;

    private String activityName;

    private Integer activityStatus;

    private LocalDateTime activityStartTime;

    private LocalDateTime activityEndTime;

}
