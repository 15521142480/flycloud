package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 积分商城商品 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PointProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private Long spuId;

    private Long skuId;

    private Integer count;

    private Integer point;

    private Integer price;

    private Integer stock;

    private Integer activityStatus;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
