package com.fly.mall.api.product.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 移动端 - 商品 SPU 明细返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "移动端 - 商品 SPU 明细返回对象")
public class AppProductSpuDetailRespVo extends AppProductSpuRespVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商品详情")
    private String description;

    @Schema(description = "SKU 数组")
    private List<Sku> skus;

    /**
     * 移动端 - 商品 SPU 明细 SKU 信息。
     */
    @Data
    @Schema(description = "移动端 - 商品 SPU 明细 SKU 信息")
    public static class Sku {

        @Schema(description = "SKU 编号")
        @JsonLongId
        private Long id;

        @Schema(description = "商品属性数组")
        private List<AppProductPropertyValueDetailRespVo> properties;

        @Schema(description = "销售价格，单位：分")
        private Integer price;

        @Schema(description = "市场价，单位：分")
        private Integer marketPrice;

        @Schema(description = "VIP 价格，单位：分")
        private Integer vipPrice;

        @Schema(description = "图片地址")
        private String picUrl;

        @Schema(description = "库存")
        private Integer stock;

        @Schema(description = "商品重量")
        private Double weight;

        @Schema(description = "商品体积")
        private Double volume;

    }

}
