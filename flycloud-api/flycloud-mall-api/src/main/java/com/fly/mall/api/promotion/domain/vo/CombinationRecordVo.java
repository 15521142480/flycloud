package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团记录 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CombinationRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private Integer combinationPrice;

    private Long spuId;

    private String spuName;

    private String picUrl;

    private Long skuId;

    private Integer count;

    private Long userId;

    private String nickname;

    private String avatar;

    private Long headId;

    private Integer status;

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
