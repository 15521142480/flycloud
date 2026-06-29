package com.fly.mall.api.product.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品 SKU 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_sku", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品SKU对象", description = "商品 SKU 表")
public class ProductSku extends BaseEntity {

    /**
     * 商品 SKU 编号。
     */
    @TableId
    private Long id;

    /**
     * 商品 SPU 编号。
     */
    private Long spuId;

    /**
     * 属性数组，JSON 格式。
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Property> properties;

    /**
     * 商品价格，单位：分。
     */
    private Integer price;

    /**
     * 市场价，单位：分。
     */
    private Integer marketPrice;

    /**
     * 成本价，单位：分。
     */
    private Integer costPrice;

    /**
     * 商品条码。
     */
    private String barCode;

    /**
     * 图片地址。
     */
    private String picUrl;

    /**
     * 库存。
     */
    private Integer stock;

    /**
     * 商品重量，单位：kg。
     */
    private Double weight;

    /**
     * 商品体积，单位：m^3。
     */
    private Double volume;

    /**
     * 一级分销佣金，单位：分。
     */
    private Integer firstBrokeragePrice;

    /**
     * 二级分销佣金，单位：分。
     */
    private Integer secondBrokeragePrice;

    /**
     * 商品销量。
     */
    private Integer salesCount;

    /**
     * 商品规格属性。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {

        /**
         * 属性编号。
         */
        private Long propertyId;

        /**
         * 属性名称。
         */
        private String propertyName;

        /**
         * 属性值编号。
         */
        private Long valueId;

        /**
         * 属性值名称。
         */
        private String valueName;

    }

}
