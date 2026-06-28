package com.fly.mall.api.domain.promotion.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 秒杀活动 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class SeckillActivityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long spuId;

    private String name;

    private Integer status;

    private String remark;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer sort;

    private List<Long> configIds;

    private Integer totalLimitCount;

    private Integer singleLimitCount;

    private Integer stock;

    private Integer totalStock;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
