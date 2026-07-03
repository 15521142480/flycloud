package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 移动端拼团活动详情返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppCombinationActivityDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer userSize;

    private Integer successCount;

    private Long spuId;

    private Integer totalLimitCount;

    private Integer singleLimitCount;

    private List<Product> products;

    /**
     * 拼团活动 SKU 信息。
     */
    @Data
    public static class Product implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long skuId;

        private Integer combinationPrice;

    }

}
