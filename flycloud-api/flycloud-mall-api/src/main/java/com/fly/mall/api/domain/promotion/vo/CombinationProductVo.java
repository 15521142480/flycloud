package com.fly.mall.api.domain.promotion.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团商品 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CombinationProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private Long spuId;

    private Long skuId;

    private Integer combinationPrice;

    private Integer activityStatus;

    private LocalDateTime activityStartTime;

    private LocalDateTime activityEndTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
