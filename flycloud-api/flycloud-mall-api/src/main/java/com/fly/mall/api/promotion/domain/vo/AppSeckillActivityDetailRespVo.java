package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 移动端秒杀活动详情返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppSeckillActivityDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JsonLongId
    private Long spuId;

    private Integer totalLimitCount;

    private Integer singleLimitCount;

    private Integer stock;

    private Integer totalStock;

    private List<Product> products;

    /**
     * 秒杀活动 SKU 信息。
     */
    @Data
    public static class Product implements Serializable {

        private static final long serialVersionUID = 1L;

        @JsonLongId
        private Long skuId;

        private Integer seckillPrice;

        private Integer stock;

    }

}
