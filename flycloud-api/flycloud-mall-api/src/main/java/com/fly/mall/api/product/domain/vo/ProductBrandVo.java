package com.fly.mall.api.product.domain.vo;

import com.fly.common.annotation.JsonLongId;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品品牌视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductBrandVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "品牌编号")
    @JsonLongId
    private Long id;

    @ExcelProperty(value = "品牌名称")
    private String name;

    @ExcelProperty(value = "品牌图片")
    private String picUrl;

    @ExcelProperty(value = "品牌排序")
    private Integer sort;

    @ExcelProperty(value = "品牌描述")
    private String description;

    @ExcelProperty(value = "品牌状态")
    private Integer status;

    @ExcelProperty(value = "是否删除")
    private Boolean isDeleted;

    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
