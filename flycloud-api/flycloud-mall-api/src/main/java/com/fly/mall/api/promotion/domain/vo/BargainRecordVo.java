package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 砍价记录 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BargainRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long activityId;

    private Long spuId;

    private Long skuId;

    private Integer bargainFirstPrice;

    private Integer bargainPrice;

    private Integer status;

    private LocalDateTime endTime;

    private Long orderId;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
