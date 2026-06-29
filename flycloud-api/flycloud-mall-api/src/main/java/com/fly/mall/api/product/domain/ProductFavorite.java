package com.fly.mall.api.product.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品收藏 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_favorite")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品收藏对象", description = "商品收藏表")
public class ProductFavorite extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Long spuId;

}
