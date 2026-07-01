package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 优惠券模板 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class CouponTemplateBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "description")
    private String description;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "totalCount")
    private Integer totalCount;

    @Schema(description = "takeLimitCount")
    private Integer takeLimitCount;

    @Schema(description = "takeType")
    private Integer takeType;

    @Schema(description = "usePrice")
    private Integer usePrice;

    @Schema(description = "productScope")
    private Integer productScope;

    @Schema(description = "productScopeValues")
    private List<Long> productScopeValues;

    @Schema(description = "validityType")
    private Integer validityType;

    @Schema(description = "validStartTime")
    private LocalDateTime validStartTime;

    @Schema(description = "validEndTime")
    private LocalDateTime validEndTime;

    @Schema(description = "fixedStartTerm")
    private Integer fixedStartTerm;

    @Schema(description = "fixedEndTerm")
    private Integer fixedEndTerm;

    @Schema(description = "discountType")
    private Integer discountType;

    @Schema(description = "discountPercent")
    private Integer discountPercent;

    @Schema(description = "discountPrice")
    private Integer discountPrice;

    @Schema(description = "discountLimitPrice")
    private Integer discountLimitPrice;

    @Schema(description = "takeCount")
    private Integer takeCount;

    @Schema(description = "useCount")
    private Integer useCount;

}
