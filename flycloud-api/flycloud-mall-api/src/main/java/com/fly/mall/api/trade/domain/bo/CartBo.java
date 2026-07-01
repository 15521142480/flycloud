package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 购物车 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class CartBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "count")
    private Integer count;

    @Schema(description = "selected")
    private Boolean selected;

    @Schema(description = "购物车编号集合")
    private List<Long> ids;

}
