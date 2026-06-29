package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品品牌 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductBrandBo extends BaseEntity {

    @Schema(description = "品牌编号", example = "1")
    private Long id;

    @Schema(description = "品牌名称", example = "FlyCloud")
    private String name;

    @Schema(description = "品牌图片")
    private String picUrl;

    @Schema(description = "品牌排序", example = "1")
    private Integer sort;

    @Schema(description = "品牌描述")
    private String description;

    @Schema(description = "品牌状态", example = "0")
    private Integer status;

}
