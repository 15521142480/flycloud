package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_coupon", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "优惠券对象", description = "优惠券表")
public class Coupon extends BaseEntity {

    @TableId
    private Long id;

    private Long templateId;

    private String name;

    private Integer status;

    private Long userId;

    private Integer takeType;

    private Integer usePrice;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    private Integer productScope;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> productScopeValues;

    private Integer discountType;

    private Integer discountPercent;

    private Integer discountPrice;

    private Integer discountLimitPrice;

    private Long useOrderId;

    private LocalDateTime useTime;

}
