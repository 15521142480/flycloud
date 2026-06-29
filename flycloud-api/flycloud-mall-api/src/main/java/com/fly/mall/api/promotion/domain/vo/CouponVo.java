package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 优惠券 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CouponVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private List<Long> productScopeValues;

    private Integer discountType;

    private Integer discountPercent;

    private Integer discountPrice;

    private Integer discountLimitPrice;

    private Long useOrderId;

    private LocalDateTime useTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
