package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 砍价助力 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BargainHelpVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long activityId;

    @JsonLongId
    private Long recordId;

    @JsonLongId
    private Long userId;

    private Integer reducePrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
