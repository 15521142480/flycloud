package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import com.fly.mall.api.product.domain.ProductSku;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品 SKU BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductSkuBo extends BaseEntity {

    @Schema(description = "SKU 编号", example = "1")
    private Long id;

    @Schema(description = "SPU 编号", example = "1")
    private Long spuId;

    @Schema(description = "规格属性数组")
    private List<ProductSku.Property> properties;

    @Schema(description = "商品价格，单位：分", example = "100")
    private Integer price;

    @Schema(description = "市场价，单位：分", example = "200")
    private Integer marketPrice;

    @Schema(description = "成本价，单位：分", example = "50")
    private Integer costPrice;

    @Schema(description = "商品条码")
    private String barCode;

    @Schema(description = "图片地址")
    private String picUrl;

    @Schema(description = "库存", example = "100")
    private Integer stock;

    @Schema(description = "商品重量，单位：kg")
    private Double weight;

    @Schema(description = "商品体积，单位：m^3")
    private Double volume;

    @Schema(description = "一级分销佣金，单位：分")
    private Integer firstBrokeragePrice;

    @Schema(description = "二级分销佣金，单位：分")
    private Integer secondBrokeragePrice;

    @Schema(description = "销量")
    private Integer salesCount;

}
