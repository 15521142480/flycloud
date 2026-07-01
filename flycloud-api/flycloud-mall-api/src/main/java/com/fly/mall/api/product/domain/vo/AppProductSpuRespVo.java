package com.fly.mall.api.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端 - 商品 SPU 返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 商品 SPU 返回对象")
public class AppProductSpuRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "SPU 编号")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品简介")
    private String introduction;

    @Schema(description = "分类编号")
    private Long categoryId;

    @Schema(description = "商品封面图")
    private String picUrl;

    @Schema(description = "商品轮播图")
    private List<String> sliderPicUrls;

    @Schema(description = "规格类型")
    private Boolean specType;

    @Schema(description = "商品价格，单位：分")
    private Integer price;

    @Schema(description = "市场价，单位：分")
    private Integer marketPrice;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "商品销量")
    private Integer salesCount;

    @Schema(description = "配送方式数组")
    private List<Integer> deliveryTypes;

}
