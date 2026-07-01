package com.fly.mall.api.product.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品浏览记录 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductBrowseHistoryBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "userDeleted")
    private Boolean userDeleted;

}
