package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端积分商城活动详情返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppPointActivityDetailRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long spuId;

    private Integer status;

    private Integer stock;

    private Integer totalStock;

    private String remark;

    private List<Product> products;

    private Integer point;

    private Integer price;

    /**
     * 积分商城活动 SKU 信息。
     */
    @Data
    public static class Product implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;

        private Long skuId;

        private Integer count;

        private Integer point;

        private Integer price;

        private Integer stock;

    }

}
