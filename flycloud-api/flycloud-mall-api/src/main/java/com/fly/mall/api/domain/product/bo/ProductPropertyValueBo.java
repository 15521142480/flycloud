package com.fly.mall.api.domain.product.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品属性值 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductPropertyValueBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "propertyId")
    private Long propertyId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "remark")
    private String remark;

}
