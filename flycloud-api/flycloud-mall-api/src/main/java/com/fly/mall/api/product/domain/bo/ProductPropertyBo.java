package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品属性 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductPropertyBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "remark")
    private String remark;

}
