package com.fly.mall.api.domain.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端 - 商品 SKU 基础返回对象。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Data
@Schema(description = "移动端 - 商品 SKU 基础返回对象")
public class AppProductSkuBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "SKU 编号")
    private Long id;

    @Schema(description = "图片地址")
    private String picUrl;

    @Schema(description = "销售价格，单位：分")
    private Integer price;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "属性数组")
    private List<AppProductPropertyValueDetailRespVo> properties;

}
