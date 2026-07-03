package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端砍价活动详情返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppBargainActivityDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long spuId;

    private Long skuId;

    private Integer price;

    private String description;

    private Integer stock;

    private String picUrl;

    private Integer marketPrice;

    private Integer bargainFirstPrice;

    private Integer bargainMinPrice;

    private Integer successUserCount;

}
