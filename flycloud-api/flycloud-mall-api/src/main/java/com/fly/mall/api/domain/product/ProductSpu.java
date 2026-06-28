package com.fly.mall.api.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 商品 SPU 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_spu", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品SPU对象", description = "商品 SPU 表")
public class ProductSpu extends BaseEntity {

    /**
     * 商品 SPU 编号。
     */
    @TableId
    private Long id;

    /**
     * 商品名称。
     */
    private String name;

    /**
     * 关键字。
     */
    private String keyword;

    /**
     * 商品简介。
     */
    private String introduction;

    /**
     * 商品详情。
     */
    private String description;

    /**
     * 商品分类编号。
     */
    private Long categoryId;

    /**
     * 商品品牌编号。
     */
    private Long brandId;

    /**
     * 商品封面图。
     */
    private String picUrl;

    /**
     * 商品轮播图。
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> sliderPicUrls;

    /**
     * 排序字段。
     */
    private Integer sort;

    /**
     * 商品状态。
     */
    private Integer status;

    /**
     * 规格类型，false 单规格，true 多规格。
     */
    private Boolean specType;

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
     * 商品库存。
     */
    private Integer stock;

    /**
     * 配送方式数组。
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> deliveryTypes;

    /**
     * 物流模板编号。
     */
    private Long deliveryTemplateId;

    /**
     * 赠送积分。
     */
    private Integer giveIntegral;

    /**
     * 是否单独设置分销佣金。
     */
    private Boolean subCommissionType;

    /**
     * 商品销量。
     */
    private Integer salesCount;

    /**
     * 虚拟销量。
     */
    private Integer virtualSalesCount;

    /**
     * 浏览量。
     */
    private Integer browseCount;

}
