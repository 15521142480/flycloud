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
 * 优惠券模板 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "promotion_coupon_template", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "优惠券模板对象", description = "优惠券模板表")
public class CouponTemplate extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String description;

    private Integer status;

    private Integer totalCount;

    private Integer takeLimitCount;

    private Integer takeType;

    private Integer usePrice;

    private Integer productScope;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> productScopeValues;

    private Integer validityType;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    private Integer fixedStartTerm;

    private Integer fixedEndTerm;

    private Integer discountType;

    private Integer discountPercent;

    private Integer discountPrice;

    private Integer discountLimitPrice;

    private Integer takeCount;

    private Integer useCount;

}
