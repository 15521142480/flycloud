package com.fly.mall.api.product.domain.vo;

import com.fly.common.annotation.JsonLongId;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品分类视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "分类编号")
    @JsonLongId
    private Long id;

    @ExcelProperty(value = "父分类编号")
    @JsonLongId
    private Long parentId;

    @ExcelProperty(value = "分类名称")
    private String name;

    @ExcelProperty(value = "分类图片")
    private String picUrl;

    @ExcelProperty(value = "分类排序")
    private Integer sort;

    @ExcelProperty(value = "分类状态")
    private Integer status;

    @ExcelProperty(value = "是否删除")
    private Boolean isDeleted;

    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
