package com.fly.mall.api.product.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品 SPU 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductSpuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "SPU 编号")
    private Long id;

    @ExcelProperty(value = "商品名称")
    private String name;

    @ExcelProperty(value = "关键字")
    private String keyword;

    @ExcelProperty(value = "商品简介")
    private String introduction;

    private String description;

    @ExcelProperty(value = "分类编号")
    private Long categoryId;

    @ExcelProperty(value = "品牌编号")
    private Long brandId;

    @ExcelProperty(value = "商品封面图")
    private String picUrl;

    private List<String> sliderPicUrls;

    @ExcelProperty(value = "排序")
    private Integer sort;

    @ExcelProperty(value = "商品状态")
    private Integer status;

    private Boolean specType;

    @ExcelProperty(value = "价格")
    private Integer price;

    @ExcelProperty(value = "市场价")
    private Integer marketPrice;

    private Integer costPrice;

    @ExcelProperty(value = "库存")
    private Integer stock;

    private List<Integer> deliveryTypes;

    private Long deliveryTemplateId;

    private Integer giveIntegral;

    private Boolean subCommissionType;

    @ExcelProperty(value = "销量")
    private Integer salesCount;

    private Integer virtualSalesCount;

    private Integer browseCount;

    private Boolean isDeleted;

    private LocalDateTime createTime;

    private List<ProductSkuVo> skus;

}
