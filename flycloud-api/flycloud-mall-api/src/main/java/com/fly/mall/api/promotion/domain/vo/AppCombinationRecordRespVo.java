package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端拼团记录返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppCombinationRecordRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long activityId;

    private String nickname;

    private String avatar;

    private LocalDateTime expireTime;

    private Integer userSize;

    private Integer userCount;

    private Integer status;

    @JsonLongId
    private Long orderId;

    private String spuName;

    private String picUrl;

    private Integer count;

    private Integer combinationPrice;

}
