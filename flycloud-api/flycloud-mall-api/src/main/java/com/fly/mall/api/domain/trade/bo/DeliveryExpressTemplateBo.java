package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 运费模板 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryExpressTemplateBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "chargeMode")
    private Integer chargeMode;

    @Schema(description = "sort")
    private Integer sort;

}
