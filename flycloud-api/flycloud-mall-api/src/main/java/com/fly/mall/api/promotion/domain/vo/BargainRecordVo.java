package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 砍价记录 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BargainRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long userId;

    @JsonLongId
    private Long activityId;

    @JsonLongId
    private Long spuId;

    @JsonLongId
    private Long skuId;

    private Integer bargainFirstPrice;

    private Integer bargainPrice;

    private Integer status;

    private LocalDateTime endTime;

    @JsonLongId
    private Long orderId;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
