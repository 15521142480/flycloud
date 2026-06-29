package com.fly.mall.api.trade.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 售后日志 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class AfterSaleLogBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "userType")
    private Integer userType;

    @Schema(description = "afterSaleId")
    private Long afterSaleId;

    @Schema(description = "beforeStatus")
    private Integer beforeStatus;

    @Schema(description = "afterStatus")
    private Integer afterStatus;

    @Schema(description = "operateType")
    private Integer operateType;

    @Schema(description = "content")
    private String content;

}
