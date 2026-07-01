package com.fly.mall.api.product.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品品牌表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("product_brand")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品品牌对象", description = "商品品牌表")
public class ProductBrand extends BaseEntity {

    /**
     * 品牌编号。
     */
    @TableId
    private Long id;

    /**
     * 品牌名称。
     */
    private String name;

    /**
     * 品牌图片。
     */
    private String picUrl;

    /**
     * 品牌排序。
     */
    private Integer sort;

    /**
     * 品牌描述。
     */
    private String description;

    /**
     * 品牌状态。
     */
    private Integer status;

}
