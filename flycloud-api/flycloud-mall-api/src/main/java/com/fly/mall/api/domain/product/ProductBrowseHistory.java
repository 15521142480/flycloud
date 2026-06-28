package com.fly.mall.api.domain.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品浏览记录 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_browse_history")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品浏览记录对象", description = "商品浏览记录表")
public class ProductBrowseHistory extends BaseEntity {

    @TableId
    private Long id;

    private Long spuId;

    private Long userId;

    private Boolean userDeleted;

}
