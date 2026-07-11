package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 秒杀商品 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class SeckillProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long activityId;

    @JsonLongId
    private List<Long> configIds;

    @JsonLongId
    private Long spuId;

    @JsonLongId
    private Long skuId;

    private Integer seckillPrice;

    private Integer stock;

    private Integer activityStatus;

    private LocalDateTime activityStartTime;

    private LocalDateTime activityEndTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
