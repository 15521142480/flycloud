package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 优惠券模板 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class CouponTemplateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private Integer status;

    private Integer totalCount;

    private Integer takeLimitCount;

    private Integer takeType;

    private Integer usePrice;

    private Integer productScope;

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

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
