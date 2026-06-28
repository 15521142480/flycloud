package com.fly.mall.api.domain.product.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品分类 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductCategoryBo extends BaseEntity {

    @Schema(description = "分类编号", example = "1")
    private Long id;

    @Schema(description = "父分类编号", example = "0")
    private Long parentId;

    @Schema(description = "分类名称", example = "手机数码")
    private String name;

    @Schema(description = "移动端分类图")
    private String picUrl;

    @Schema(description = "分类排序", example = "1")
    private Integer sort;

    @Schema(description = "分类状态", example = "0")
    private Integer status;

}
