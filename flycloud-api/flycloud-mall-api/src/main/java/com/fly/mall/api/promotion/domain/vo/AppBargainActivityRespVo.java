package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端砍价活动列表返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppBargainActivityRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JsonLongId
    private Long spuId;

    @JsonLongId
    private Long skuId;

    private Integer stock;

    private String picUrl;

    private Integer marketPrice;

    private Integer bargainMinPrice;

}
