package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 优惠券 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CouponBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "templateId")
    private Long templateId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "takeType")
    private Integer takeType;

    @Schema(description = "usePrice")
    private Integer usePrice;

    @Schema(description = "validStartTime")
    private LocalDateTime validStartTime;

    @Schema(description = "validEndTime")
    private LocalDateTime validEndTime;

    @Schema(description = "productScope")
    private Integer productScope;

    @Schema(description = "productScopeValues")
    private List<Long> productScopeValues;

    @Schema(description = "discountType")
    private Integer discountType;

    @Schema(description = "discountPercent")
    private Integer discountPercent;

    @Schema(description = "discountPrice")
    private Integer discountPrice;

    @Schema(description = "discountLimitPrice")
    private Integer discountLimitPrice;

    @Schema(description = "useOrderId")
    private Long useOrderId;

    @Schema(description = "useTime")
    private LocalDateTime useTime;

}
