package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 限时折扣商品 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DiscountProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
