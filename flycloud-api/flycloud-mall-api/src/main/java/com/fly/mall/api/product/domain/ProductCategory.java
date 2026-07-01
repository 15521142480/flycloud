package com.fly.mall.api.product.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("product_category")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品分类对象", description = "商品分类表")
public class ProductCategory extends BaseEntity {

    /**
     * 根分类编号。
     */
    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 分类编号。
     */
    @TableId
    private Long id;

    /**
     * 父分类编号。
     */
    private Long parentId;

    /**
     * 分类名称。
     */
    private String name;

    /**
     * 移动端分类图。
     */
    private String picUrl;

    /**
     * 分类排序。
     */
    private Integer sort;

    /**
     * 分类状态。
     */
    private Integer status;

}
