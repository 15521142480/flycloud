package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 砍价助力 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BargainHelpVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private Long recordId;

    private Long userId;

    private Integer reducePrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
