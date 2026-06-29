package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 快递公司 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class DeliveryExpressBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "code")
    private String code;

    @Schema(description = "name")
    private String name;

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "sort")
    private Integer sort;

    @Schema(description = "status")
    private Integer status;

}
