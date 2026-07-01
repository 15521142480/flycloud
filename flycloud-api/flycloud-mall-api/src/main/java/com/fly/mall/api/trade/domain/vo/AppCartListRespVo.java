package com.fly.mall.api.trade.domain.vo;

import com.fly.mall.api.product.domain.vo.AppProductSkuBaseRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuBaseRespVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端 - 购物车列表返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 购物车列表返回对象")
public class AppCartListRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "有效购物项数组")
    private List<Cart> validList;

    @Schema(description = "无效购物项数组")
    private List<Cart> invalidList;

    /**
     * 移动端 - 购物项。
     */
    @Data
    @Schema(description = "移动端 - 购物项")
    public static class Cart implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "购物项编号")
        private Long id;

        @Schema(description = "商品数量")
        private Integer count;

        @Schema(description = "是否选中")
        private Boolean selected;

        @Schema(description = "商品 SPU")
        private AppProductSpuBaseRespVo spu;

        @Schema(description = "商品 SKU")
        private AppProductSkuBaseRespVo sku;

    }

}
