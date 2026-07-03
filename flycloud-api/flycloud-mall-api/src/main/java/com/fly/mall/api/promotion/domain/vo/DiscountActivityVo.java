package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 限时折扣活动 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class DiscountActivityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String remark;

    /**
     * 限时折扣商品列表。
     */
    private List<DiscountProductVo> products;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
