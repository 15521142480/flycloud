package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 砍价活动 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BargainActivityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    @JsonLongId
    private Long spuId;

    @JsonLongId
    private Long skuId;

    private Integer bargainFirstPrice;

    private Integer bargainMinPrice;

    private Integer stock;

    private Integer totalStock;

    private Integer helpMaxCount;

    private Integer bargainCount;

    private Integer totalLimitCount;

    private Integer randomMinPrice;

    private Integer randomMaxPrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
