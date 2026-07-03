package com.fly.mall.api.promotion.domain.vo;

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

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long spuId;

    private Long skuId;

    private Integer stock;

    private String picUrl;

    private Integer marketPrice;

    private Integer bargainMinPrice;

}
