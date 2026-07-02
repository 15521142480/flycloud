package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品 SPU BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductSpuBo extends BaseEntity {

    /**
     * 出售中商品。
     */
    public static final Integer FOR_SALE = 0;

    /**
     * 仓库中商品。
     */
    public static final Integer IN_WAREHOUSE = 1;

    /**
     * 已售空商品。
     */
    public static final Integer SOLD_OUT = 2;

    /**
     * 警戒库存商品。
     */
    public static final Integer ALERT_STOCK = 3;

    /**
     * 商品回收站。
     */
    public static final Integer RECYCLE_BIN = 4;

    @Schema(description = "SPU 编号", example = "1")
    private Long id;

    @Schema(description = "商品名称", example = "飞翔云周边")
    private String name;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "商品简介")
    private String introduction;

    @Schema(description = "商品详情")
    private String description;

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "品牌编号", example = "1")
    private Long brandId;

    @Schema(description = "商品封面图")
    private String picUrl;

    @Schema(description = "商品轮播图")
    private List<String> sliderPicUrls;

    @Schema(description = "排序字段", example = "1")
    private Integer sort;

    @Schema(description = "商品状态", example = "0")
    private Integer status;

    @Schema(description = "前端请求的 Tab 类型", example = "1")
    private Integer tabType;

    @Schema(description = "规格类型，false 单规格，true 多规格")
    private Boolean specType;

    @Schema(description = "商品价格，单位：分")
    private Integer price;

    @Schema(description = "市场价，单位：分")
    private Integer marketPrice;

    @Schema(description = "成本价，单位：分")
    private Integer costPrice;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "配送方式数组")
    private List<Integer> deliveryTypes;

    @Schema(description = "物流模板编号")
    private Long deliveryTemplateId;

    @Schema(description = "赠送积分")
    private Integer giveIntegral;

    @Schema(description = "是否单独设置分销佣金")
    private Boolean subCommissionType;

    @Schema(description = "商品销量")
    private Integer salesCount;

    @Schema(description = "虚拟销量")
    private Integer virtualSalesCount;

    @Schema(description = "浏览量")
    private Integer browseCount;

    @Schema(description = "商品 SKU 列表")
    private List<ProductSkuBo> skus;

}
