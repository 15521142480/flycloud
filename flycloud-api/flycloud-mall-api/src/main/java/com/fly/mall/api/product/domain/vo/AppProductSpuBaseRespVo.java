package com.fly.mall.api.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 商品 SPU 基础返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 商品 SPU 基础返回对象")
public class AppProductSpuBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "SPU 编号")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品主图地址")
    private String picUrl;

    @Schema(description = "商品分类编号")
    private Long categoryId;

    @Schema(description = "商品库存")
    private Integer stock;

    @Schema(description = "商品状态")
    private Integer status;

}
