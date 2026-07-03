package com.fly.mall.api.promotion.domain.vo;

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

    private Long id;

    private Long spuId;

    private Long skuId;

    private Long activityId;

    private Integer status;

    private Integer bargainPrice;

    private String activityName;

    private LocalDateTime endTime;

    private String picUrl;

    private Long orderId;

    private Boolean payStatus;

    private Long payOrderId;

}
