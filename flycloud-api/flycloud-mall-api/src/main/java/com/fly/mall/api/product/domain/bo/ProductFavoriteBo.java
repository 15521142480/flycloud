package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品收藏 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class ProductFavoriteBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "spuId")
    private Long spuId;

}
