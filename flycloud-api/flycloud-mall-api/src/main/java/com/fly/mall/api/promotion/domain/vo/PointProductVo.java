package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

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

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long activityId;

    @JsonLongId
    private Long spuId;

    @JsonLongId
    private Long skuId;

    private Integer count;

    private Integer point;

    private Integer price;

    private Integer stock;

    private Integer activityStatus;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
