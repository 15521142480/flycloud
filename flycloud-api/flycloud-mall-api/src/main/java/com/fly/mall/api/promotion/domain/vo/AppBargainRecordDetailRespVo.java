package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端砍价记录详情返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppBargainRecordDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int HELP_ACTION_NONE = 1;
    public static final int HELP_ACTION_FULL = 2;
    public static final int HELP_ACTION_SUCCESS = 3;

    private Long id;

    private Long userId;

    private Long spuId;

    private Long skuId;

    private Long activityId;

    private Integer bargainFirstPrice;

    private Integer bargainPrice;

    private Integer status;

    private Long orderId;

    private Boolean payStatus;

    private Long payOrderId;

    private Integer helpAction;

}
