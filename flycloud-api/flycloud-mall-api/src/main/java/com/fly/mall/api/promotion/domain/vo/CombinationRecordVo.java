package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团记录 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class CombinationRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long activityId;

    private Integer combinationPrice;

    @JsonLongId
    private Long spuId;

    private String spuName;

    private String picUrl;

    @JsonLongId
    private Long skuId;

    private Integer count;

    @JsonLongId
    private Long userId;

    private String nickname;

    private String avatar;

    @JsonLongId
    private Long headId;

    private Integer status;

    @JsonLongId
    private Long orderId;

    private Integer userSize;

    private Integer userCount;

    private Boolean virtualGroup;

    private LocalDateTime expireTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
