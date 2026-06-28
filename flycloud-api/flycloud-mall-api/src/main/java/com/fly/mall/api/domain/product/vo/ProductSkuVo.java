package com.fly.mall.api.domain.product.vo;

import com.fly.mall.api.domain.product.ProductSku;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品 SKU 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductSkuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long spuId;

    private List<ProductSku.Property> properties;

    private Integer price;

    private Integer marketPrice;

    private Integer costPrice;

    private String barCode;

    private String picUrl;

    private Integer stock;

    private Double weight;

    private Double volume;

    private Integer firstBrokeragePrice;

    private Integer secondBrokeragePrice;

    private Integer salesCount;

}
