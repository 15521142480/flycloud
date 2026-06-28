package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import com.fly.mall.api.domain.promotion.RewardActivity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 满减送活动 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class RewardActivityBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "startTime")
    private LocalDateTime startTime;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "conditionType")
    private Integer conditionType;

    @Schema(description = "productScope")
    private Integer productScope;

    @Schema(description = "productScopeValues")
    private List<Long> productScopeValues;

    @Schema(description = "rules")
    private List<RewardActivity.Rule> rules;

    @Schema(description = "limit")
    private Integer limit;

    @Schema(description = "discountPrice")
    private Integer discountPrice;

    @Schema(description = "freeDelivery")
    private Boolean freeDelivery;

    @Schema(description = "point")
    private Integer point;

    @Schema(description = "giveCouponTemplateCounts")
    private Map<Long, Integer> giveCouponTemplateCounts;

}
