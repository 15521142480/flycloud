package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团活动 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class CombinationActivityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long spuId;

    private Integer totalLimitCount;

    private Integer singleLimitCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer userSize;

    private Boolean virtualGroup;

    private Integer status;

    private Integer limitDuration;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
