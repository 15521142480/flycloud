package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端砍价记录返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppBargainRecordRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long spuId;

    @JsonLongId
    private Long skuId;

    @JsonLongId
    private Long activityId;

    private Integer status;

    private Integer bargainPrice;

    private String activityName;

    private LocalDateTime endTime;

    private String picUrl;

    @JsonLongId
    private Long orderId;

    private Boolean payStatus;

    @JsonLongId
    private Long payOrderId;

}
