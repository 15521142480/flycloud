package com.fly.mall.api.domain.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品属性值 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_property_value")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品属性值对象", description = "商品属性值表")
public class ProductPropertyValue extends BaseEntity {

    @TableId
    private Long id;

    private Long propertyId;

    private String name;

    private String remark;

}
